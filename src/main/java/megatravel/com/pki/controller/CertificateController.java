package megatravel.com.pki.controller;

import megatravel.com.pki.domain.DTO.CertificateRequestDTO;
import megatravel.com.pki.domain.DTO.ServerDTO;
import megatravel.com.pki.domain.enums.CerType;
import megatravel.com.pki.service.CertificateService;
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

    @GetMapping
    public ResponseEntity<List<ServerDTO>> findAll() {
        logger.info("Requesting all available servers at time {}.", Calendar.getInstance().getTime());
        certificateService.setServer("root");
        certificateService.createRootCertificate(new X500Principal("CN=Root, OU=HQ, O=MegaTravel, C=UK"));
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
}
