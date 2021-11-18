package ee.gaile.controller.user;

import ee.gaile.service.security.request.LoginRequest;
import ee.gaile.service.security.request.SignupRequest;
import ee.gaile.service.security.response.MessageResponse;
import ee.gaile.service.security.settings.ApiErrorException;
import ee.gaile.service.security.settings.AuthRefreshDTO;
import ee.gaile.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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

/**
 * REST service controller for user authorization and registration
 *
 * @author Aleksei Gaile
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "UserController", description = "Controller for authorization and user registration")
public class UserController {
    private static final Logger ACCESS_LOG = LoggerFactory.getLogger("access-accounting-log");
    private final UserService userService;

    @PostMapping(path = "/login")
    @Operation(summary = "Service for user authorization")
    public ResponseEntity<LoginRequest> getUserLoginToken(@RequestBody SignupRequest auth, HttpServletRequest request) {
        ACCESS_LOG.info("user access request, user name is {}, IP is {} ", auth.getUsername(), request.getHeader("userIP"));
        return new ResponseEntity<>(userService.authUser(auth, request), HttpStatus.OK);
    }

    @PostMapping(path = "/refresh")
    @Operation(summary = "Token renewal service")
    public ResponseEntity<AuthRefreshDTO> getAccessTokenByRefreshToken(@RequestBody AuthRefreshDTO authDTO,
                                                                       HttpServletRequest request) throws ApiErrorException {
        return new ResponseEntity<>(userService.refreshAuth(authDTO, request), HttpStatus.OK);
    }

    @PostMapping("/register")
    @Operation(summary = "User registration service")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return userService.registerUser(signUpRequest);
    }

}
