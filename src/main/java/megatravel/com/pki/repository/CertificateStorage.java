package megatravel.com.pki.repository;

import megatravel.com.pki.config.AppConfig;
import megatravel.com.pki.domain.cert.IssuerData;
import megatravel.com.pki.domain.enums.CerType;
import megatravel.com.pki.util.CerAndKey;
import megatravel.com.pki.util.GeneralException;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
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
                return new IssuerData((PrivateKey) key,
                        new JcaX509CertificateHolder(cert).getSubject(), cert.getPublicKey(), cert.getSerialNumber());
            } else {
                throw new GeneralException("Error occurred while storing certificate!",
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException |
                UnrecoverableKeyException e) {
            throw new GeneralException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public CerAndKey[] load(String alias, String password, String keystore,
                            String server, CerType type) {
        try {
            KeyStore keyStore = KeyStore.getInstance("jks");
            Path storage;
            if (type == CerType.USER) {
                storage = Paths.get("src", "main", "resources", "users",
                        keystore, "trust", "storage.jks");
            } else {
                storage = Paths.get("src", "main", "resources", "servers", server,
                        keystore, "trust", "storage.jks");
            }
            keyStore.load(new FileInputStream(storage.toString()), password.toCharArray());
            Key key = keyStore.getKey(alias, password.toCharArray());

            if (key instanceof PrivateKey) {
                Certificate[] certs = keyStore.getCertificateChain(alias);
                return CerAndKey.toChain(certs, key);
            } else {
                throw new GeneralException("Error occurred while storing certificate!",
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException |
                CertificateException | UnrecoverableKeyException | ClassCastException e) {
            throw new GeneralException("Error occurred while reading certificate!",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void store(CerAndKey[] cks, String server, CerType type, String alias, String password) {
        CerAndKey leaf = cks[0];
        Path storage, trustStorage;
        if (type == CerType.USER) {
            storage = Paths.get("src", "main", "resources", "servers", server, "users",
                    leaf.getCertificate().getSerialNumber().toString());
            trustStorage = Paths.get("src", "main", "resources", "servers", server, "users",
                    leaf.getCertificate().getSerialNumber().toString(), "trust");
        } else if (type == CerType.INTERMEDIATE) {
            storage = Paths.get("src", "main", "resources", "servers", server, "sub",
                    leaf.getCertificate().getSerialNumber().toString());
            trustStorage = Paths.get("src", "main", "resources", "servers", server, "sub",
                    leaf.getCertificate().getSerialNumber().toString(), "trust");
        } else {
            storage = Paths.get("src", "main", "resources", "servers", server,
                    leaf.getCertificate().getSerialNumber().toString());
            trustStorage = Paths.get("src", "main", "resources", "servers", server,
                    leaf.getCertificate().getSerialNumber().toString(), "trust");
        }
        try {

            Files.createDirectories(trustStorage);
            FileOutputStream out = new FileOutputStream(Paths.get(storage.toString(),
                    "cer_" + leaf.getCertificate().getSerialNumber() + ".cer").toString());
            out.write(leaf.getCertificate().getEncoded());
            out.close();

            storePrivateKey(alias, password.toCharArray(),
                    Paths.get(trustStorage.toString(), "storage.jks").toString(), leaf.getPrivateKey(),
                    CerAndKey.toChain(cks));
        } catch (Exception e) {
            throw new GeneralException("Error occurred while storing certificate!",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public X509Certificate[] findCertificates(List<String> serialNumbers) {
        List<X509Certificate> result = new ArrayList<>();
        String rootPath = Paths.get("src", "main", "resources", "servers").toString();

        File root = new File(rootPath);
        try {
            for (File node : Objects.requireNonNull(root.listFiles())) {
                if (serialNumbers.isEmpty()) {
                    return result.toArray(new X509Certificate[0]);
                }
                for (File subNode : Objects.requireNonNull(node.listFiles())) {
                    if (serialNumbers.isEmpty()) {
                        return result.toArray(new X509Certificate[0]);
                    }
                    if (subNode.getName().equals("users") || subNode.getName().equals("sub")) {
                        for (File subSubNode : Objects.requireNonNull(subNode.listFiles())) {
                            if (serialNumbers.isEmpty()) {
                                return result.toArray(new X509Certificate[0]);
                            }
                            if (serialNumbers.contains(subSubNode.getName())) {
                                result.add(readCertificate(Paths.get(subSubNode.getPath(),
                                        "cer_" + subSubNode.getName() + ".cer").toString()));
                                serialNumbers.remove(subSubNode.getName());
                            }
                        }
                    } else if (serialNumbers.contains(subNode.getName())) {
                        result.add(readCertificate(Paths.get(subNode.getPath(),
                                "cer_" + subNode.getName() + ".cer").toString()));
                        serialNumbers.remove(subNode.getName());
                    }
                }
            }
            return result.toArray(new X509Certificate[0]);
        } catch (NullPointerException | FileNotFoundException | CertificateException e) {
            throw new GeneralException("Error occurred while searching certificates!",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String[] findTarget(String target) {
        List<String> found = new ArrayList<>();
        String rootPath = Paths.get("src", "main", "resources", "servers").toString();

        File root = new File(rootPath);
        try {
            for (File node : Objects.requireNonNull(root.listFiles())) {
                for (File subNode : Objects.requireNonNull(node.listFiles())) {
                    if (subNode.getName().equals("users")) {
                        for (File subSubNode : Objects.requireNonNull(subNode.listFiles())) {
                            if (target.equals(subSubNode.getName())) {
                                return new String[]{
                                        Paths.get(subSubNode.getPath(), "trust", "trust_storage.jks")
                                                .toString(), Paths.get("src", "main", "resources", "users",
                                        target, "trust", "trust_storage.jks").toString(),
                                };
                            }
                        }
                    } else if (subNode.getName().equals("sub")) {
                        for (File subSubNode : Objects.requireNonNull(subNode.listFiles())) {
                            if (target.equals(subSubNode.getName())) {
                                found.add(subSubNode.getPath());
                            }
                        }
                    } else if (target.equals(subNode.getName())) {
                        found.add(subNode.getPath());
                    }
                }
            }
            found.removeIf(path -> path.contains("/sub/"));
            return new String[]{Paths.get(found.get(0), "trust", "trust_storage.jks").toString()};
        } catch (NullPointerException e) {
            throw new GeneralException("Error occurred while searching target certificate!",
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

    private X509Certificate readCertificate(String name) throws FileNotFoundException, CertificateException {
        FileInputStream fis = new FileInputStream(name);
        BufferedInputStream bis = new BufferedInputStream(fis);

        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Certificate cert = cf.generateCertificate(bis);
        return (X509Certificate) cert;
    }
}
