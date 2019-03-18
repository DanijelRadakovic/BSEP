package megatravel.com.pki.service;

import megatravel.com.pki.domain.Certificate;
import megatravel.com.pki.repository.CertificateRepository;
import megatravel.com.pki.repository.CertificateStorage;
import megatravel.com.pki.util.CerAndKey;
import megatravel.com.pki.util.GeneralException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sun.security.tools.keytool.CertAndKeyGen;
import sun.security.x509.*;

import javax.security.auth.x500.X500Principal;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

@Service
public class CertificateService {

    @Autowired
    private CertificateStorage certificateStorage;

    @Autowired
    private CertificateRepository certificateRepository;

    private String server;

    public void createRootCertificate(X500Principal root) {
        CertAndKeyGen gen = generateKeyPair();
        CertificateExtensions exts = new CertificateExtensions();
        try {
            exts.set(BasicConstraintsExtension.NAME, new BasicConstraintsExtension(true, -1));
            X509Certificate certificate = gen.getSelfCertificate(X500Name.asX500Name(root),
                    new Date(), (long) 365 * 24 * 3600, exts);
            CerAndKey ck = new CerAndKey(certificate, gen.getPrivateKey());
            certificateStorage.store(new CerAndKey[]{ck}, server, false, "keys", "zgadija");
            certificateRepository.save(new Certificate(null, ck.getCertificate().getSerialNumber().toString(),
                    ck.getCertificate().getSubjectDN().getName(), true));
        } catch (IOException | CertificateException | InvalidKeyException | SignatureException
                | NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new GeneralException("Error occurred while generating certificate!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void createSignedCertificate(X500Principal subject, String issuer, boolean user) {
        CerAndKey[] chain = certificateStorage.load("keys", "zgadija", issuer, server);
        Principal issuerData = chain[0].getCertificate().getSubjectDN();
        String issuerSigAlg = chain[0].getCertificate().getSigAlgName();

        CertAndKeyGen sub = generateKeyPair();
        try {
            X509Certificate certificate = sub.getSelfCertificate(X500Name.asX500Name(subject),
                    (long) 365 * 24 * 3600);
            X509CertInfo info = new X509CertInfo(certificate.getTBSCertificate());
            info.set(X509CertInfo.ISSUER, issuerData);

            CertificateExtensions exts = new CertificateExtensions();
            if (user) {
                exts.set(BasicConstraintsExtension.NAME, new BasicConstraintsExtension(false, -1));
            } else {
                exts.set(BasicConstraintsExtension.NAME, new BasicConstraintsExtension(true, -1));
            }

            info.set(X509CertInfo.EXTENSIONS, exts);
            X509CertImpl outCert = new X509CertImpl(info);
            outCert.sign(chain[0].getPrivateKey(), issuerSigAlg);

            chain[0].setPrivateKey(null); // do not store privet key of issuer
            CerAndKey[] expendedChain = new CerAndKey[chain.length + 1];
            expendedChain[0] = new CerAndKey(outCert, sub.getPrivateKey());
            System.arraycopy(chain, 0, expendedChain, 1, chain.length);
            certificateStorage.store(expendedChain, server, false, "keys", "zgadija");
            certificateRepository.save(new Certificate(null, expendedChain[0].getCertificate().
                    getSerialNumber().toString(), expendedChain[0].getCertificate()
                    .getSubjectDN().getName(), true));
        } catch (CertificateException | InvalidKeyException | SignatureException |
                NoSuchAlgorithmException | NoSuchProviderException | IOException e) {
            throw new GeneralException("Error occurred while generating certificate!", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (DataIntegrityViolationException e) {
            throw new GeneralException("Serial number is not unique!", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    private CertAndKeyGen generateKeyPair() {
        try {
            CertAndKeyGen keyGen = new CertAndKeyGen("RSA", "SHA256WithRSA", null);
            keyGen.generate(4096);
            keyGen.setRandom(SecureRandom.getInstance("SHA1PRNG", "SUN"));
            return keyGen;
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchProviderException e) {
            throw new GeneralException("Invalid key generator configuration", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void setServer(String server) {
        this.server = server;
    }
}
