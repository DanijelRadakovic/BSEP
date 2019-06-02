package megatravel.com.pki.repository;

import megatravel.com.pki.config.AppConfig;
import megatravel.com.pki.domain.cert.IssuerData;
import megatravel.com.pki.util.GeneralException;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.*;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class CertificateStorage {

    @Autowired
    private AppConfig config;

    public void store(X509Certificate[] chain, PrivateKey privateKey) {
        char[] password = config.getKeystorePassword().toCharArray();
        String serialNumber = chain[0].getSerialNumber().toString();
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            try {
                keyStore.load(new FileInputStream(config.getKeystore()), password);
            } catch (IOException e) {
                keyStore.load(null, null);
            }

            keyStore.setKeyEntry(serialNumber, privateKey, serialNumber.toCharArray(), chain);
            keyStore.store(new FileOutputStream(config.getKeystore()), password);
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            throw new GeneralException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public IssuerData findCAbySerialNumber(String serialNumber) {
        char[] password = config.getKeystorePassword().toCharArray();
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(new FileInputStream(config.getKeystore()), password);

            Key key = keyStore.getKey(serialNumber, serialNumber.toCharArray());
            if (key instanceof PrivateKey) {
                X509Certificate cert = (X509Certificate) keyStore.getCertificate(serialNumber);
                return new IssuerData((PrivateKey) key, new JcaX509CertificateHolder(cert).getSubject(),
                        cert.getPublicKey(), cert.getSerialNumber());
            } else {
                throw new GeneralException("Error occurred while storing certificate!",
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException |
                UnrecoverableKeyException e) {
            throw new GeneralException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void createTrustStorage(X509Certificate cert) {
        try {
            File directory = new File(config.getTruststore());
            if (!directory.exists()) {
                if (!directory.mkdirs()) throw new IOException("Can not create directory!");
            }
            KeyStore trustStore = KeyStore.getInstance("PKCS12");
            trustStore.load(null, null);

            trustStore.store(new FileOutputStream(config.getTruststore() + File.separator +
                    cert.getSerialNumber() + ".p12"), config.getTruststorePassword().toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            throw new GeneralException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<X509Certificate> getCertificates(List<String> serialNumbers) {
        List<X509Certificate> certs = new ArrayList<>(serialNumbers.size());
        char[] password = config.getKeystorePassword().toCharArray();
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(new FileInputStream(config.getKeystore()), password);

            serialNumbers.forEach(s -> {
                try {
                    certs.add((X509Certificate) keyStore.getCertificate(s));
                } catch (KeyStoreException e) {
                    throw new GeneralException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                }
            });
            return certs;
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException e) {
            throw new GeneralException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private X509Certificate readCertificate(String name) throws FileNotFoundException, CertificateException {
        FileInputStream fis = new FileInputStream(name);
        BufferedInputStream bis = new BufferedInputStream(fis);

        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Certificate cert = cf.generateCertificate(bis);
        return (X509Certificate) cert;
    }
}
