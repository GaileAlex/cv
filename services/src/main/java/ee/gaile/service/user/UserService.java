package ee.gaile.service.user;

import ee.gaile.entity.enums.EnumRoles;
import ee.gaile.entity.models.Role;
import ee.gaile.entity.models.User;
import ee.gaile.service.repository.UserRepositoryTemp;
import ee.gaile.service.security.LoginService;
import ee.gaile.service.security.request.LoginRequest;
import ee.gaile.service.security.request.SignupRequest;
import ee.gaile.service.security.response.MessageResponse;
import ee.gaile.service.security.settings.ApiErrorException;
import ee.gaile.service.security.settings.AuthRefreshDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
@AllArgsConstructor
public class UserService {
    private final LoginService loginService;
    private final UserRepositoryTemp userRepository;
    private final PasswordEncoder encoder;

    public LoginRequest authUser(SignupRequest auth) throws ApiErrorException {
        return loginService.authUser(auth);
    }

    public AuthRefreshDTO refreshAuth(AuthRefreshDTO authDTO, HttpServletRequest request) throws ApiErrorException {
        return loginService.refreshAuth(authDTO, request);
    }

    public ResponseEntity<?> registerUser(SignupRequest signUpRequest){
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role userRole = new Role(EnumRoles.ROLE_USER);
        roles.add(userRole);
        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
