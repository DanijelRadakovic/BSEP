package megatravel.com.pki.controller;

import megatravel.com.pki.converter.ServerConverter;
import megatravel.com.pki.domain.DTO.ServerDTO;
import megatravel.com.pki.domain.Server;
import megatravel.com.pki.service.ServerService;
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
@RequestMapping("/api/server")
public class ServerController {

    private static final Logger logger = LoggerFactory.getLogger(ServerController.class);

    @Autowired
    private ServerService serverService;

    /**
     * GET /api/server
     *
     * @return all available servers
     */
    @GetMapping
    public ResponseEntity<List<ServerDTO>> findAll() {
        logger.info("Requesting all available servers at time {}.", Calendar.getInstance().getTime());
        return new ResponseEntity<>(ServerConverter.fromEntityList(serverService.getAll(), ServerDTO::new),
                HttpStatus.OK);
    }

    /**
     * GET /api/server/{id}
     *
     * @param id of requested server
     * @return server with requested id
     */
    @GetMapping("{id}")
    public ResponseEntity<ServerDTO> findById(@PathVariable String id) {
        logger.info("Requesting server with id {} at time {}.", id, Calendar.getInstance().getTime());
        return new ResponseEntity<>(new ServerDTO(serverService.findById(Long.parseLong(id))), HttpStatus.FOUND);
    }

    /**
     * POST /api/server
     *
     * @param server that needs to be added
     * @return added vehicle
     */
    @PostMapping()
    //@PreAuthorize("hasAuthority('SECADMIN')")
    public ResponseEntity<ServerDTO> save(@RequestBody ServerDTO server) {
        logger.info("Adding vehicle with at time {}.", Calendar.getInstance().getTime());
        return new ResponseEntity<>(new ServerDTO(serverService.save(new Server(server))), HttpStatus.OK);
    }

    /**
     * DELETE /api/server/{id}
     *
     * @param id of vehicle that needs to be deleted
     * @return message about action results
     */
    @DeleteMapping(value = "{id}", produces = MediaType.TEXT_PLAIN_VALUE)
    //@PreAuthorize("hasAuthority('SECADMIN')")
    public ResponseEntity<String> delete(@PathVariable String id) {
        logger.info("Deleting server at time {}.", Calendar.getInstance().getTime());
        serverService.remove(Long.parseLong(id));
        return new ResponseEntity<>("Server successfully deleted!", HttpStatus.OK);
    }
}
