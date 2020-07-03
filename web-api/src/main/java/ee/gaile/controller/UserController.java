package ee.gaile.controller;

import ee.gaile.service.security.request.LoginRequest;
import ee.gaile.service.security.request.SignupRequest;
import ee.gaile.service.security.settings.ApiErrorException;
import ee.gaile.service.security.settings.AuthRefreshDTO;
import ee.gaile.service.user.UserService;
import lombok.AllArgsConstructor;
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
    /*protected Logger log = LoggerFactory.getLogger(this.getClass());*/

    UserService userService;

    @PostMapping(path = "/login", produces = "application/json")
    public LoginRequest getUserLoginToken(@RequestBody SignupRequest auth) throws ApiErrorException {
        return userService.authUser(auth);
    }

    @PostMapping(path = "/refresh", produces = "application/json")
    public AuthRefreshDTO getAccessTokenByRefreshToken(@RequestBody AuthRefreshDTO authDTO, HttpServletRequest request) throws Exception {
        return userService.refreshAuth(authDTO, request);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return userService.registerUser(signUpRequest);
    }
}
