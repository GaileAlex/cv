package ee.gaile.controller.user;

import ee.gaile.service.security.request.LoginRequest;
import ee.gaile.service.security.request.SignupRequest;
import ee.gaile.service.security.settings.AuthRefreshDTO;
import ee.gaile.service.user.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    private static final Logger ACCESS_LOG = LoggerFactory.getLogger("access-accounting-log");

    @PostMapping(path = "/login")
    public ResponseEntity<LoginRequest> getUserLoginToken(@RequestBody SignupRequest auth, HttpServletRequest request) {
        ACCESS_LOG.info("user access request, user name is {}, IP is {} ", auth.getUsername(), request.getHeader("userIP"));
        return new ResponseEntity<>(userService.authUser(auth, request), HttpStatus.OK);
    }

    @PostMapping(path = "/refresh")
    public ResponseEntity<AuthRefreshDTO> getAccessTokenByRefreshToken(@RequestBody AuthRefreshDTO authDTO,
                                                                       HttpServletRequest request) throws Exception {
        return new ResponseEntity<>(userService.refreshAuth(authDTO, request), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return userService.registerUser(signUpRequest);
    }

}
