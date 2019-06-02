package megatravel.com.pki.service;

import megatravel.com.pki.repository.CertificateStorage;
import megatravel.com.pki.util.GeneralException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Service
public class TransportService {

    @Autowired
    private CertificateStorage certificateStorage;

//    public void sendCertificate(CerAndKey[] cks, String destination, boolean user) {
//        CerAndKey leaf = cks[0];
//        Path storage, trustStorage;
//        if (user) {
//            storage = Paths.get("src", "main", "resources", "users",
//                    leaf.getCertificate().getSerialNumber().toString());
//            trustStorage = Paths.get("src", "main", "resources", "users",
//                    leaf.getCertificate().getSerialNumber().toString(), "trust");
//        } else {
//            storage = Paths.get("src", "main", "resources", "servers", destination,
//                    leaf.getCertificate().getSerialNumber().toString());
//            trustStorage = Paths.get("src", "main", "resources", "servers", destination,
//                    leaf.getCertificate().getSerialNumber().toString(), "trust");
//        }
//        try {
//
//            Files.createDirectories(trustStorage);
//            FileOutputStream out = new FileOutputStream(Paths.get(storage.toString(),
//                    "cer_" + leaf.getCertificate().getSerialNumber() + ".cer").toString());
//            out.write(leaf.getCertificate().getEncoded());
//            out.close();
//            storePrivateKey("keys", "zgadija".toCharArray(),
//                    Paths.get(trustStorage.toString(), "storage.jks").toString(), leaf.getPrivateKey(),
//                    CerAndKey.toChain(cks));
//        } catch (Exception e) {
//            throw new GeneralException("Error occurred while storing certificate!",
//                    HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    public void sendTrustStorage(KeyStore keyStore, String destination) {
//        try {
//            keyStore.store(new FileOutputStream(destination), "zgadija".toCharArray());
//        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException e) {
//            throw new GeneralException("Error occurred while sending trust storage!",
//                    HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    private void storePrivateKey(String alias, char[] password, String keystore, Key key, X509Certificate[] chain)
//            throws Exception {
//        KeyStore keyStore = KeyStore.getInstance("jks");
//        keyStore.load(null, null);
//
//        keyStore.setKeyEntry(alias, key, password, chain);
//        keyStore.store(new FileOutputStream(keystore), password);
//    }
}
