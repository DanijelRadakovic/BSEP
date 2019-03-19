package megatravel.com.pki.service;

import megatravel.com.pki.repository.CertificateStorage;
import megatravel.com.pki.util.GeneralException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TrustStorageService {

    @Autowired
    private CertificateStorage certificateStorage;

    @Autowired
    private TransportService transportService;

    public void updateTrustStorage(String target, List<String> serialNumbers) {
        X509Certificate[] cers = certificateStorage.findCertificates(serialNumbers);
        String[] paths = certificateStorage.findTarget(target);

        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(4096, random);
            KeyStore keyStore = KeyStore.getInstance("jks");
            keyStore.load(null, null);

            keyStore.setKeyEntry("keys", keyGen.genKeyPair().getPrivate(), "zgadija".toCharArray(), cers);
            keyStore.store(new FileOutputStream(paths[0]), "zgadija".toCharArray());
            if (paths.length == 2) {
                transportService.sendTrustStorage(keyStore, paths[1]);
            }
        } catch (NoSuchAlgorithmException | NoSuchProviderException |
                KeyStoreException | IOException | CertificateException e) {
            throw new GeneralException("Error occurred while updating trust storage!",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Map<String, String> getTrustStorage(String target) {
        String[] paths = certificateStorage.findTarget(target);
        Map<String, String> result = new HashMap<>();
        try {
            KeyStore keyStore = KeyStore.getInstance("jks");
            keyStore.load(new FileInputStream(paths[0]), "zgadija".toCharArray());

            Certificate[] certs = keyStore.getCertificateChain("keys");
            int i = certs.length;
            for (Certificate cer : certs) {
                X509Certificate x509 = (X509Certificate) cer;
                result.put(x509.getSerialNumber().toString(), x509.getSubjectDN().getName());
            }
            return result;
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException e) {
            throw new GeneralException("Error occurred while reading trust storage!",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
