package megatravel.com.pki.repository;

import megatravel.com.pki.util.CerAndKey;
import megatravel.com.pki.util.GeneralException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Repository
public class CertificateRepository {


    public CerAndKey[] load(String alias, String password, String keystore, String server) {
        try {
            KeyStore keyStore = KeyStore.getInstance("jks");
            Path storage = Paths.get("src", "main", "resources", "servers", server,
                    keystore, "trust", "storage.jks");
            keyStore.load(new FileInputStream(storage.toString()), password.toCharArray());
            Key key = keyStore.getKey(alias, password.toCharArray());

            if (key instanceof PrivateKey) {
                Certificate[] certs = keyStore.getCertificateChain(alias);
                return CerAndKey.toChain(certs, key);
            } else {
                throw new GeneralException("Error occurred while storing certificate",
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException |
                CertificateException | UnrecoverableKeyException | ClassCastException e) {
            throw new GeneralException("Error occurred while reading certificate",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void store(CerAndKey[] cks, String server, boolean user, String alias, String password) {
        CerAndKey leaf = cks[0];
        Path storage = Paths.get("src", "main", "resources", "servers", server,
                leaf.getCertificate().getSerialNumber().toString());
        Path trustStorage = Paths.get("src", "main", "resources", "servers", server,
                leaf.getCertificate().getSerialNumber().toString(), "trust");
        try {
            if (!user) {
                Files.createDirectories(trustStorage);
                FileOutputStream out = new FileOutputStream(Paths.get(storage.toString(),
                        "cer_" + leaf.getCertificate().getSerialNumber() + ".cer").toString());
                out.write(leaf.getCertificate().getEncoded());
                out.close();
            }
            storePrivateKey(alias, password.toCharArray(),
                    Paths.get(trustStorage.toString(), "storage.jks").toString(), leaf.getPrivateKey(),
                    new X509Certificate[]{leaf.getCertificate()});
        } catch (Exception e) {
            throw new GeneralException("Error occurred while storing certificate",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void storePrivateKey(String alias, char[] password, String keystore, Key key, X509Certificate[] chain)
            throws Exception {
        KeyStore keyStore = KeyStore.getInstance("jks");
        keyStore.load(null, null);

        keyStore.setKeyEntry(alias, key, password, chain);
        keyStore.store(new FileOutputStream(keystore), password);
    }
}
