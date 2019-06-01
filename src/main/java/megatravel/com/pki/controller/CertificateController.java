package megatravel.com.pki.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import megatravel.com.pki.converter.CertificateConverter;
import megatravel.com.pki.domain.cert.Certificate;
import megatravel.com.pki.domain.DTO.CertificateDTO;
import megatravel.com.pki.domain.DTO.CertificateRequestDTO;
import megatravel.com.pki.domain.enums.CerType;
import megatravel.com.pki.service.CertificateGeneratorService;
import megatravel.com.pki.service.CertificateService;
import megatravel.com.pki.service.TransportService;
import megatravel.com.pki.util.CerAndKey;
import megatravel.com.pki.util.ValidationException;
import org.bouncycastle.asn1.x500.X500Name;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.x500.X500Principal;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


@RestController
@RequestMapping("/api/cer")
public class CertificateController extends ValidationController {

//    @Bean
//    public RestTemplate restTemplate(RestTemplateBuilder builder) {
//        // Do any additional configuration here
//        return builder.build();
//    }

//    @Autowired
//    private RestTemplate restTemplate;
//
//    @GetMapping("/test")
//    ResponseEntity<String> someRandomFunction() {
//        restTemplate.getForObject("https://localhost:8081/api/cer", String.class);
//        return new ResponseEntity<>("Successfully established connection with server localhost:8081!", HttpStatus.OK);
//    }
//
//    @GetMapping("/test2")
//    ResponseEntity<String> someRandomFunction2() {
//        restTemplate.getForObject("https://localhost:8082/api/cer", String.class);
//        return new ResponseEntity<>("Successfully established connection with server localhost:8082!", HttpStatus.OK);
//    }

    private static final Logger logger = LoggerFactory.getLogger(CertificateController.class);

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private CertificateGeneratorService generatorService;

    @Autowired
    private TransportService transportService;


    @GetMapping
//    @PreAuthorize("hasAuthority('GENCERT')")
    public ResponseEntity<List<CertificateDTO>> getAll() {
        logger.info("Requesting all available certificates at time {}.", Calendar.getInstance().getTime());
        return new ResponseEntity<>(CertificateConverter.fromEntityList(certificateService.findAll(),
                CertificateDTO::new), HttpStatus.OK);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<CertificateDTO>> findAll() {
        logger.info("Requesting all available certificates at time {}.", Calendar.getInstance().getTime());
        List<CertificateDTO> dtoList = new ArrayList<>();
        List<Certificate> certificateList = certificateService.findAll();
        for (Certificate c : certificateList) {
            dtoList.add(new CertificateDTO(c));
        }
        return new ResponseEntity<>(dtoList, HttpStatus.OK);

//        CerAndKey[] ck = certificateRepository.load("keys", "zgadija",
//                "327109625", "root");
//        for (CerAndKey c : cks) {
//            logger.info(c.getCertificate().toString());
//        }


    }

    /**
     * POST /api/cer
     *
     * @param request that needs to be processed
     * @return added vehicle
     */
    @PostMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    //@PreAuthorize("hasAuthority('SECADMIN')")
    public ResponseEntity<String> save(@RequestBody String request) throws IOException, ValidationException {
//        logger.info("Generating certificate at time {}.", Calendar.getInstance().getTime());
//        certificateService.setServer(request.getServer());
//        CerAndKey[] chain;
//        if (request.getType() == CerType.ROOT) {
//            certificateService.createRootCertificate(new X500Principal(request.getSubjectDN()));
//            return new ResponseEntity<>("Certificate successfully created!", HttpStatus.OK);
//        } else if (request.getType() == CerType.INTERMEDIATE) {
//            chain = certificateService.createSignedCertificate(new X500Principal(request.getSubjectDN()),
//                    request.getIssuerSN().getSerialNumber(), request.getType());
//            if (request.getDestination() != null && !request.getDestination().equals("")) {
//                transportService.sendCertificate(chain, request.getDestination(), false);
//            }
//        } else {
//            chain = certificateService.createSignedCertificate(new X500Principal(request.getSubjectDN()),
//                    request.getIssuerSN().getSerialNumber(), request.getType());
//            transportService.sendCertificate(chain, "users", true);
//        }
        CertificateRequestDTO temp = new ObjectMapper().readValue(request, CertificateRequestDTO.class);
        X500Name name = CertificateConverter.toX500Name(temp.getSubjectDN(), temp.getType());
        System.out.println(name.toString());
        generatorService.save(name, temp.getIssuerSN(), temp.getType());
        return new ResponseEntity<>("Certificate successfully created!", HttpStatus.OK);
    }

    /**
     * DELETE /api/cer/remove/{id}
     *
     * @param id of certificate that needs to be deleted
     * @return message about action results
     */
    @DeleteMapping(value = "/remove/{id}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> delete(@PathVariable String id) {
        logger.info("Deleting certificate at time {}.", Calendar.getInstance().getTime());
        certificateService.remove(Long.parseLong(id));
        return new ResponseEntity<>("Certificate successfully deleted!", HttpStatus.OK);
    }
}
