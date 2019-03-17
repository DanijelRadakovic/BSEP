package megatravel.com.pki.service;

import sun.security.x509.X500Name;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public class CertificateService {

    public X509Certificate createSignedCertificate(X500Name subject, X509Certificate issuerCertificate,
                                                   PrivateKey issuerPrivateKey) {

        return null;
    }


}
