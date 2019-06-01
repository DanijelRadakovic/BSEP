package megatravel.com.pki.controller;

import megatravel.com.pki.domain.cert.IssuerData;
import megatravel.com.pki.domain.cert.SubjectData;
import megatravel.com.pki.service.CertificateGeneratorService;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileWriter;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private CertificateGeneratorService cg;

    @GetMapping
    public ResponseEntity<String> findAll() {
//        try {
//            SubjectData subjectData = cg.generateSubjectData();
//
//            KeyPair keyPairIssuer = cg.generateKeyPair(false);
//            IssuerData issuerData = cg.generateIssuerData(keyPairIssuer.getPrivate());
//
//            X509Certificate cert = cg.generateCertificate(subjectData, issuerData);
//
//            System.out.println("\n===== Podaci o izdavacu sertifikata =====");
//            System.out.println(cert.getIssuerX500Principal().getName());
//            System.out.println("\n===== Podaci o vlasniku sertifikata =====");
//            System.out.println(cert.getSubjectX500Principal().getName());
//            System.out.println("\n===== Sertifikat =====");
//            System.out.println("-------------------------------------------------------");
//            System.out.println(cert);
//            System.out.println("-------------------------------------------------------");
//
//            cert.verify(keyPairIssuer.getPublic());
//            System.out.println("\nValidacija uspesna :)");
//
////            KeyPair anotherPair = cg.generateKeyPair(false);
////            cert.verify(anotherPair.getPublic());
//            JcaPEMWriter pemWrt = new JcaPEMWriter(new FileWriter("./temp/cert.pem"));
//            pemWrt.writeObject(cert);
//            pemWrt.flush();
//            pemWrt.close();
//        } catch (CertificateException | InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException e) {
//            e.printStackTrace();
//        } catch (SignatureException e) {
//            System.out.println("\nValidacija neuspesna :(");
//            // e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return new ResponseEntity<>("nesto", HttpStatus.OK);
    }
}
