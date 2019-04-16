package megatravel.com.pki.controller;

import megatravel.com.pki.converter.UserConverter;
import megatravel.com.pki.domain.DTO.*;
import megatravel.com.pki.domain.User;
import megatravel.com.pki.security.TokenUtils;
import megatravel.com.pki.service.UserService;
import megatravel.com.pki.util.GeneralException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;

import javax.validation.Valid;
import java.util.Calendar;
import java.util.List;

@Controller
@RequestMapping("/api/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenUtils tokenUtils;

    /**
     * Endpoint for request for getting use by id
     * @param id - given id through url
     * @return user with given ID with OK, or BAD_REQUEST if snot found with given ID
     */
    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id){
        try{
            logger.info("Trying to find user with id=" + id + " at " +  Calendar.getInstance().getTime());
            User user = userService.findById(id);
            logger.info("Successfully found user with id=" + id + " at " + Calendar.getInstance().getTime());
            return new ResponseEntity<>(UserConverter.fromRegisteringEntity(user), HttpStatus.OK);
        }catch(GeneralException e){
            logger.warn("User with id=" + id + "now found, time: " + Calendar.getInstance().getTime());
            return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
        }catch(Exception e1){
            logger.error("Failed to get user with id=" + id + ", error message: "
                    + e1.getMessage() + ", time: " + Calendar.getInstance().getTime());
            return new ResponseEntity<>(e1.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint for getting all users
     * @return list of users, or BAD_REQUEST if something went wrong
     */
    @GetMapping()
    public ResponseEntity<List<User>> getAllUsers(){
        try {
            logger.info("Fetching all users at " + Calendar.getInstance().getTime());
            return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
        }catch(Exception e){
            logger.error("Failed to get all users, error message: " + e.getMessage()
                    + ", time: " + Calendar.getInstance().getTime());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

//    /**
//     * Endpoint for logging in of all types of users
//     * @param loginDTO - dto with required credentials
//     * @return user if successfully logged in, or message if error happened
//     */
//    @PostMapping(value = "login", produces = "application/text")
//
//    public ResponseEntity<Object> login(@RequestBody LoginDTO loginDTO){
//        try{
//            logger.info("Logging in... " + Calendar.getInstance().getTime());
//            User user = userService.logIn(loginDTO.getUsername(), loginDTO.getPassword());
//            if (user == null){
//                logger.warn("Unsuccessful log password for " + loginDTO.getUsername() + " at "
//                        + Calendar.getInstance().getTime());
//                return new ResponseEntity<>("Incorrect password!", HttpStatus.BAD_REQUEST);
//            }
//            logger.info("Successfully logged in, " + loginDTO.getUsername() + ". " + Calendar.getInstance().getTime());
//            return new ResponseEntity<>("Successfully logged in", HttpStatus.OK);
//        }catch(GeneralException e){
//            logger.warn("Unsuccessful log username:" + loginDTO.getUsername() + " at "
//                    + Calendar.getInstance().getTime());
//            return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
//        }catch(Exception e1){
//            logger.error("Error while trying to log in happened, message: " + e1.getMessage() + ", time: "
//                    + Calendar.getInstance().getTime());
//            return new ResponseEntity<>(e1.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//    }


    /**
     * POST /api/user/auth
     * <p>
     * Authenticates a user in the system.
     *
     * @param authenticationRequest DTO with user's login credentials
     * @return ResponseEntity with a AuthenticationResponseDTO, containing user data and his JSON Web Token
     */
    @PostMapping("/auth")
    public ResponseEntity<Object> login(@Valid @RequestBody AuthenticationRequestDTO authenticationRequest) {
        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword());

            Authentication authentication = authenticationManager.authenticate(authToken);

            //SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
            LoggedUserDTO user = UserConverter.fromLoggedEntity(userService.findByUsername(userDetails.getUsername()));
            String token = tokenUtils.generateToken(userDetails);

            logger.info("Successfully logged in.");
            return new ResponseEntity<>(new AuthenticationResponseDTO(user, token), HttpStatus.OK);
            //}
        /*logger.info("Failed to login, incorrect combination od username and password");
        return new ResponseEntity<Object>("Incorrect username or password, or user is not activated.",
                HttpStatus.BAD_REQUEST);*/
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    /**
     * Endpoint for registering new user
     * @param registeringDTO - all required data for new user
     * @return user if successfully registered, or message if error happened
     */
    @PostMapping("register")
    public ResponseEntity<Object> register(@RequestBody RegisteringDTO registeringDTO){
        try{
            logger.info("Trying to register new user with username: " + registeringDTO.getUsername() +
                    " at " + Calendar.getInstance().getTime());
            User user = userService.register(UserConverter.toRegisteringEntity(registeringDTO));
            logger.info("Successfully registered user with username " + registeringDTO.getUsername() +
                    " at " + Calendar.getInstance().getTime());
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        }catch(GeneralException e){
            logger.warn("User registration failed, username " + registeringDTO.getUsername() +
                    " already exists, time " + Calendar.getInstance().getTime());
            return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
        }catch(Exception e1){
            logger.error("Error while trying to register happened, message: " + e1.getMessage() + ", time: "
                    + Calendar.getInstance().getTime());
            return new ResponseEntity<>(e1.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
