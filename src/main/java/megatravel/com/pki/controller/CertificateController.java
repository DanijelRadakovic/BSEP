package megatravel.com.pki.controller;

import megatravel.com.pki.domain.DTO.CertificateRequestDTO;
import megatravel.com.pki.domain.DTO.ServerDTO;
import megatravel.com.pki.domain.enums.CerType;
import megatravel.com.pki.service.CertificateService;
import megatravel.com.pki.service.TransportService;
import megatravel.com.pki.util.CerAndKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.x500.X500Principal;
import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping("/api/cer")
public class CertificateController {

    private static final Logger logger = LoggerFactory.getLogger(CertificateController.class);

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private TransportService transportService;

    @GetMapping
    public ResponseEntity<List<ServerDTO>> findAll() {
        logger.info("Requesting all available servers at time {}.", Calendar.getInstance().getTime());
        //CerAndKey[] cks = certificateStorage.load("keys", "zgadija", "1712030549", "london", CerType.USER);
//        List<Certificate> temp = certificateRepository.findBySerialNumber();
//        logger.info(temp.size() + "");
//        CerAndKey[] ck = certificateRepository.load("keys", "zgadija",
//                "327109625", "root");
//        for (CerAndKey c : cks) {
//            logger.info(c.getCertificate().toString());
//        }
        return null;
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
        CerAndKey[] chain;
        if (request.getType() == CerType.ROOT) {
            certificateService.createRootCertificate(new X500Principal(request.getX500Name()));
            return new ResponseEntity<>("Certificate successfully created!", HttpStatus.OK);
        } else if (request.getType() == CerType.INTERMEDIATE) {
            chain = certificateService.createSignedCertificate(new X500Principal(request.getX500Name()),
                    request.getIssuer().getSerialNumber(), request.getType());
            if (request.getDestination() != null && !request.getDestination().equals("")) {
                transportService.sendCertificate(chain, request.getDestination(), false);
            }
        } else {
            chain = certificateService.createSignedCertificate(new X500Principal(request.getX500Name()),
                    request.getIssuer().getSerialNumber(), request.getType());
            transportService.sendCertificate(chain, "users", true);
        }

        return new ResponseEntity<>("Certificate successfully created!", HttpStatus.OK);
    }
}
