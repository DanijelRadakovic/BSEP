package megatravel.com.pki.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import megatravel.com.pki.converter.CertificateConverter;
import megatravel.com.pki.domain.DTO.CertificateDTO;
import megatravel.com.pki.domain.DTO.CertificateRequestDTO;
import megatravel.com.pki.service.CertificateService;
import megatravel.com.pki.util.ValidationException;
import org.bouncycastle.asn1.x500.X500Name;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/cer")
public class CertificateController extends ValidationController {

    private static final Logger logger = LoggerFactory.getLogger(CertificateController.class);

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private CertificateService generatorService;

    /**
     * GET /api/cer
     *
     * @return all available certificates
     */
    @GetMapping
//    @PreAuthorize("hasAuthority('GENCERT')")
    public ResponseEntity<List<CertificateDTO>> getAll() {
        logger.info("action=getAllCertificates status=success");
        return new ResponseEntity<>(CertificateConverter.fromEntityList(certificateService.findAll(),
                CertificateDTO::new), HttpStatus.OK);
    }

    /**
     * GET /api/cer/active
     *
     * @return all available active certificates
     */
    @GetMapping("/active")
//    @PreAuthorize("hasAuthority('GENCERT')")
    public ResponseEntity<List<CertificateDTO>> getAllActive() {
        logger.info("action=getAllActiveCertificates status=success");
        return new ResponseEntity<>(CertificateConverter.fromEntityList(certificateService.findAllActive(),
                CertificateDTO::new), HttpStatus.OK);
    }

    /**
     * GET /api/cer/ca
     *
     * @return all available certificate authorities
     */
    @GetMapping("/ca")
    public ResponseEntity<List<CertificateDTO>> getAllCA() {
        logger.info("action=getAllCA status=success");
        return new ResponseEntity<>(CertificateConverter.fromEntityList(certificateService.findAllCA(),
                CertificateDTO::new), HttpStatus.OK);
    }

    /**
     * GET /api/cer/ca/active
     *
     * @return all available active certificate authorities
     */
    @GetMapping("/ca/active")
    public ResponseEntity<List<CertificateDTO>> getAllActiveCA() {
        logger.info("action=getAllActiveCA status=success");
        return new ResponseEntity<>(CertificateConverter.fromEntityList(certificateService.findAllActiveCA(),
                CertificateDTO::new), HttpStatus.OK);
    }

    /**
     * GET /api/cer/client
     *
     * @return all available client certificates
     */
    @GetMapping("/client")
    public ResponseEntity<List<CertificateDTO>> getAllClients() {
        logger.info("action=getAllClients status=success");
        return new ResponseEntity<>(CertificateConverter.fromEntityList(certificateService.findAllClients(),
                CertificateDTO::new), HttpStatus.OK);
    }

    /**
     * GET /api/cer/client/active
     *
     * @return all available active client certificates
     */
    @GetMapping("/client/active")
    public ResponseEntity<List<CertificateDTO>> getAllActiveClients() {
        logger.info("action=getAllClients status=success");
        return new ResponseEntity<>(CertificateConverter.fromEntityList(certificateService.findAllActiveClients(),
                CertificateDTO::new), HttpStatus.OK);
    }

    /**
     * POST /api/cer
     *
     * @param request that needs to be processed
     * @return message about action results
     */
    @PostMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    //@PreAuthorize("hasAuthority('SECADMIN')")
    public ResponseEntity<String> save(@RequestBody String request) throws IOException, ValidationException {
        validateJSON(request, "certificate.json");
        CertificateRequestDTO cer = new ObjectMapper().readValue(request, CertificateRequestDTO.class);
        X500Name name = CertificateConverter.toX500Name(cer.getSubjectDN(), cer.getType());
        generatorService.save(name, cer.getIssuerSN(), cer.getType());
        logger.info("action=generateCert type={} dn={} status=success", cer.getType(), name);
        return new ResponseEntity<>("Certificate successfully created!", HttpStatus.OK);
    }

    /**
     * DELETE /api/cer/{id}
     *
     * @param id of certificate that needs to be deleted
     * @return message about action results
     */
    @DeleteMapping(value = "/{id}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> delete(@PathVariable String id) {
        certificateService.remove(Long.parseLong(id));
        logger.info("action=removeCert certId={} status=success", id);
        return new ResponseEntity<>("Certificate successfully deleted!", HttpStatus.OK);
    }
}

//TODO izmeniti da se koriste p12 storovi za bazu umesto jks