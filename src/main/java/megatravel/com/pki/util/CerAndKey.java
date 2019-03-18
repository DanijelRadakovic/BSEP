package megatravel.com.pki.util;

import java.security.Key;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

public class CerAndKey {

    private X509Certificate certificate;
    private PrivateKey privateKey;

    public CerAndKey() {
    }

    public CerAndKey(X509Certificate certificate, PrivateKey privateKey) {
        this.certificate = certificate;
        this.privateKey = privateKey;
    }

    public static CerAndKey[] toChain(Certificate[] certificates, Key privateKey) {
        CerAndKey[] chain = new CerAndKey[certificates.length];
        boolean passedLeaf = false;
        for (int i = 0; i < certificates.length; i++) {
            if (!passedLeaf) {
                chain[i] = new CerAndKey((X509Certificate) certificates[i], (PrivateKey) privateKey);
                passedLeaf = true;
            } else {
                chain[i] = new CerAndKey((X509Certificate) certificates[i], null);
            }
        }
        return chain;
    }

    public static X509Certificate[] toChain(CerAndKey[] certificates) {
        X509Certificate[] chain = new X509Certificate[certificates.length];
        for (int i = 0; i < certificates.length; i++) {
            chain[i] = certificates[i].getCertificate();
        }
        return chain;
    }

    public X509Certificate getCertificate() {
        return certificate;
    }

    public void setCertificate(X509Certificate certificate) {
        this.certificate = certificate;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }
}
