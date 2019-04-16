package megatravel.com.pki.controller;

import megatravel.com.pki.converter.CertificateConverter;
import megatravel.com.pki.domain.DTO.CertificateDTO;
import megatravel.com.pki.domain.DTO.TrustStorageDTO;
import megatravel.com.pki.service.TrustStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping("/api/trust")
public class TrustStorageController {

    private static final Logger logger = LoggerFactory.getLogger(TrustStorageController.class);

    @Autowired
    private TrustStorageService trustStorageService;


    /**
     * GET /api/trust/{id}
     *
     * @param id of requested certificate
     * @return server with requested id
     */
    @GetMapping("{id}")
    public ResponseEntity<List<CertificateDTO>> findTrustStorage(@PathVariable String id) {
        logger.info("Requesting trust storage with serial number {} at time {}.", id, Calendar.getInstance().getTime());
        return new ResponseEntity<>(CertificateConverter.fromMapToDTO(trustStorageService.getTrustStorage(id)),
                HttpStatus.OK);
    }

    /**
     * POST /api/trust
     *
     * @param request that needs to be processed
     * @return added vehicle
     */
    @PostMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    //@PreAuthorize("hasAuthority('SECADMIN')")
    public ResponseEntity<String> generate(@RequestBody TrustStorageDTO request) {
        logger.info("Updating certificate trust storage at time {}.", Calendar.getInstance().getTime());
        trustStorageService.updateTrustStorage(request.getTarget(), request.getSerialNumbers());
        return new ResponseEntity<>("Trust storage successfully updated!", HttpStatus.OK);
    }
}
