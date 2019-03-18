package megatravel.com.pki.domain.certificate;

import sun.security.tools.keytool.CertAndKeyGen;
import sun.security.x509.*;

import javax.security.auth.x500.X500Principal;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        try {
            CertAndKeyGen keyGen = new CertAndKeyGen("RSA", "SHA256WithRSA", null);
            keyGen.generate(4096);
            keyGen.setRandom(SecureRandom.getInstance("SHA1PRNG", "SUN"));

            //Generate self signed certificate
            X509Certificate[] chain = new X509Certificate[2];
            CertificateExtensions exts = new CertificateExtensions();
            exts.set(BasicConstraintsExtension.NAME, new BasicConstraintsExtension(false, -1));
            //exts.set(KeyUsageExtension.NAME, new KeyUsageExtension().);

//                    new ArrayList<AccessDescription>() {{
//                        add(new AccessDescription(new ObjectIdentifier("2.5.29.35"),
//                                new GeneralName(new URIName("https://www.megatravel.com"))));
//                    }});
            chain[0] = keyGen.getSelfCertificate(
                    X500Name.asX500Name(new X500Principal("CN=Root, OU=HQ, O=MegaTravel, C=UK")),
                    new Date(), (long) 365 * 24 * 3600, exts);
            System.out.println(chain[0].getSubjectX500Principal());
            System.out.println("Certificate : " + chain[0].toString());

            // Add AIA extension
            List<AccessDescription> access = new ArrayList<>();
            AccessDescription ocsp = new AccessDescription(AccessDescription.Ad_OCSP_Id,
                    new GeneralName(new URIName("https://esample.com")));
            access.add(ocsp);

            exts.set(AuthorityInfoAccessExtension.NAME, access);
            chain[1] = keyGen.getSelfCertificate(
                    X500Name.asX500Name(new X500Principal("CN=Root2, OU=HQ, O=MegaTravel, C=UK")),
                    new Date(), (long) 365 * 24 * 3600, exts);
            KeyStore keyStore = KeyStore.getInstance("jks");
            keyStore.load(null, null);

//            keyStore.setKeyEntry(alias, key, password, chain);
//            keyStore.store(new FileOutputStream(keystore), password);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}