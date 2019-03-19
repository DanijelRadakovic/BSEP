package megatravel.com.pki.service;

import megatravel.com.pki.repository.CertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.util.Date;

@Service
public class VerificationService {

    @Autowired
    private CertificateRepository databaseRepository;

    private boolean checkDate(X509Certificate cert)
    {
        Date startDate = cert.getNotBefore();
        Date endDate = cert.getNotAfter();
        Date today = new Date();

        return today.after(startDate) && today.before(endDate);
    }

    private boolean verifyCertificate(X509Certificate cert){
        boolean isActive = databaseRepository.findBySerialNumber(cert.getSerialNumber().longValue()).get(0).isActive();
        boolean isValidDate = checkDate(cert);
        return isActive && isValidDate;
    }

    private boolean verifySignature(X509Certificate certificate, X509Certificate parentCertificate)
    {
        try{
            certificate.verify(parentCertificate.getPublicKey());
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }

    public byte[] verifyCertificateChain(X509Certificate[] certificateList, String folderAddress)
    {
        KeyStoreReader keyStoreReader = new KeyStoreReader();

        PrivateKey privateKey = keyStoreReader.readPrivateKey(folderAddress,
                "keyStorePassword", "keys", "zgadija");
        boolean valid = true;
        for (int i = 0; i < certificateList.length - 1; i++) {
            X509Certificate cert = certificateList[i];
            if (!verifyCertificate(cert) || !verifySignature(cert, certificateList[i + 1]) )
                valid = false;
        }
        if (valid)
        {
            Signature sig = null;
            try {
                sig = Signature.getInstance("SHA1withRSA");
                sig.initSign(privateKey);
                sig.update("Valid".getBytes());
                return sig.sign();
            } catch (Exception e) {
                return new byte[0];
            }
        }
        return new byte[0];
    }

}