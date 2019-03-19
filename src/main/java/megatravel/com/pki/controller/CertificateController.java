package megatravel.com.pki.controller;

import megatravel.com.pki.domain.Certificate;
import megatravel.com.pki.domain.DTO.CertificateDTO;
import megatravel.com.pki.domain.DTO.CertificateRequestDTO;
import megatravel.com.pki.domain.DTO.ServerDTO;
import megatravel.com.pki.domain.enums.CerType;
import megatravel.com.pki.repository.CertificateRepository;
import megatravel.com.pki.service.CertificateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.x500.X500Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping("/api/cer")
public class CertificateController {

    private static final Logger logger = LoggerFactory.getLogger(CertificateController.class);

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private CertificateRepository certificateRepository;

    @GetMapping("/findAll")
    public ResponseEntity<List<CertificateDTO>> findAll() {
        logger.info("Requesting all available servers at time {}.", Calendar.getInstance().getTime());
        List<CertificateDTO> dtoList = new ArrayList<>();
        List<Certificate> certificateList =  certificateRepository.findAll();
        for (Certificate c: certificateList) {
            dtoList.add(new CertificateDTO(c));
        }
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
//        CerAndKey[] ck = certificateRepository.load("keys", "zgadija",
//                "327109625", "root");
//        for (CerAndKey c : ck) {
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
    public ResponseEntity<String> generate(@RequestBody CertificateRequestDTO request) {
        logger.info("Generating certificate at time {}.", Calendar.getInstance().getTime());
        certificateService.setServer(request.getServer());
        if (request.getType() == CerType.ROOT) {
            certificateService.createRootCertificate(new X500Principal(request.getX500Name()));
        } else if (request.getType() == CerType.INTERMEDIATE) {
            certificateService.createSignedCertificate(new X500Principal(request.getX500Name()),
                    request.getIssuer().getSerialNumber(), false);
        } else {
            certificateService.createSignedCertificate(new X500Principal(request.getX500Name()),
                    request.getIssuer().getSerialNumber(), true);
        }
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
