package megatravel.com.pki.service;

import megatravel.com.pki.config.AppConfig;
import megatravel.com.pki.domain.cert.Certificate;
import megatravel.com.pki.domain.cert.IssuerData;
import megatravel.com.pki.domain.cert.SubjectData;
import megatravel.com.pki.domain.enums.CerType;
import megatravel.com.pki.domain.enums.PeriodUnit;
import megatravel.com.pki.repository.CertificateRepository;
import megatravel.com.pki.repository.CertificateStorage;
import megatravel.com.pki.util.GeneralException;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;

@Service
public class CertificateGeneratorService {

    @Autowired
    private AppConfig config;

    @Autowired
    private CertificateStorage storage;

    @Autowired
    private CertificateRepository certificateRepository;

    public CertificateGeneratorService() {
    }

    public void save(X500Name subjectDN, String issuerSN, CerType type) {
        KeyPair keyPair;
        SubjectData subject;
        IssuerData issuer;
        X509Certificate certificate;

        if (issuerSN == null || type == CerType.ROOT) {
            keyPair = generateKeyPair(true);
            subject = generateSubjectData(keyPair.getPublic(), subjectDN, true);
            issuer = new IssuerData(keyPair.getPrivate(), subjectDN, subject.getPublicKey(), subject.getSerialNumber());
            certificate = generateCertificate(subject, issuer, true);
        } else {
            keyPair = generateKeyPair(false);
            issuer = storage.findCAbySerialNumber(issuerSN);
            subject = generateSubjectData(keyPair.getPublic(), subjectDN, false);
            certificate = generateCertificate(subject, issuer, false);
        }
        System.out.println(certificate);
        try {
            certificateRepository.save(new Certificate(null, certificate.getSerialNumber().toString(),
                    certificate.getSubjectDN().toString(), true));
            storage.store(new X509Certificate[]{certificate}, keyPair.getPrivate());
        } catch (DataIntegrityViolationException e) {
            throw new GeneralException("Subject name or serial number is not unique!", HttpStatus.BAD_REQUEST);
        }
    }

    private X509Certificate generateCertificate(SubjectData subjectData, IssuerData issuerData, boolean isCA) {
        try {
            ContentSigner contentSigner = new JcaContentSignerBuilder(
                    config.getSignatureAlgorithm())
                    .setProvider(config.getProvider()).build(issuerData.getPrivateKey());

            X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(issuerData.getX500name(),
                    subjectData.getSerialNumber(),
                    subjectData.getStartDate(),
                    subjectData.getEndDate(),
                    subjectData.getX500name(),
                    subjectData.getPublicKey());

            BasicConstraints basicConstraints = new BasicConstraints(isCA);
            certGen.addExtension(new ASN1ObjectIdentifier("2.5.29.19"), true, basicConstraints);

            JcaX509ExtensionUtils extensionUtils = new JcaX509ExtensionUtils();

            AuthorityKeyIdentifier authorityKeyIdentifier = extensionUtils
                    .createAuthorityKeyIdentifier(issuerData.getPublicKey());
            certGen.addExtension(new ASN1ObjectIdentifier("2.5.29.35"), false, authorityKeyIdentifier);

            SubjectKeyIdentifier subjectKeyIdentifier = extensionUtils
                    .createSubjectKeyIdentifier(subjectData.getPublicKey());
            certGen.addExtension(new ASN1ObjectIdentifier("2.5.29.14"), false, subjectKeyIdentifier);

            return new JcaX509CertificateConverter().setProvider(config.getProvider())
                    .getCertificate(certGen.build(contentSigner));
        } catch (IllegalArgumentException | IllegalStateException | OperatorCreationException |
                CertificateException | CertIOException | NoSuchAlgorithmException e) {
            throw new GeneralException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private KeyPair generateKeyPair(boolean isCA) {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(config.getKeyAlgorithm());
            SecureRandom random = SecureRandom.getInstance(config.getSeedAlgorithm(), config.getSeedProvider());
            if (isCA) keyGen.initialize(config.getCaKeySize(), random);
            else keyGen.initialize(config.getClientKeysize(), random);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new GeneralException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private SubjectData generateSubjectData(PublicKey publicKey, X500Name subjectDN, boolean isCA) {
        long now = System.currentTimeMillis();
        Date startDate = new Date(now);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        Date endDate = getValidityPeriod(calendar, isCA);
        return new SubjectData(publicKey, subjectDN, new BigInteger(Long.toString(now)), startDate, endDate);
    }

    private Date getValidityPeriod(Calendar now, boolean isCA) {
        if (config.getPeriodUnit() == PeriodUnit.DAY && isCA) {
            now.add(Calendar.DATE, config.getCaPeriod());
            return now.getTime();
        } else if (config.getPeriodUnit() == PeriodUnit.DAY && !isCA) {
            now.add(Calendar.DATE, config.getClientPeriod());
            return now.getTime();
        } else if (config.getPeriodUnit() == PeriodUnit.MONTH && isCA) {
            now.add(Calendar.MONTH, config.getCaPeriod());
            return now.getTime();
        } else if (config.getPeriodUnit() == PeriodUnit.MONTH && !isCA) {
            now.add(Calendar.MONTH, config.getClientPeriod());
            return now.getTime();
        } else if (config.getPeriodUnit() == PeriodUnit.YEAR && isCA) {
            now.add(Calendar.YEAR, config.getCaPeriod());
            return now.getTime();
        } else {
            now.add(Calendar.YEAR, config.getClientPeriod());
            return now.getTime();
        }
    }
}
