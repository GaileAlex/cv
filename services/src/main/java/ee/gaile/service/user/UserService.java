package ee.gaile.service.user;

import ee.gaile.service.security.request.LoginRequest;
import ee.gaile.service.security.request.SignupRequest;
import ee.gaile.service.security.response.MessageResponse;
import ee.gaile.service.security.settings.ApiErrorException;
import ee.gaile.service.security.settings.AuthRefreshDTO;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Aleksei Gaile 19-Sep-21
 */
public interface UserService {

    /**
     * Verifies the user
     *
     * @param signupRequest - user information
     * @param request       - user ID
     * @return - registered user
     */
    LoginRequest authUser(SignupRequest signupRequest, HttpServletRequest request);

    /**
     * Updates the authorization token
     *
     * @param authDTO - token
     * @param request - HttpServletRequest
     * @return - new token
     * @throws ApiErrorException - token update error
     */
    AuthRefreshDTO refreshAuth(AuthRefreshDTO authDTO, HttpServletRequest request) throws ApiErrorException;

    /**
     * Registers a user
     *
     * @param signUpRequest - user information
     * @return - registration result
     */
    ResponseEntity<MessageResponse> registerUser(SignupRequest signUpRequest);

}
