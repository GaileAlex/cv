package ee.gaile.service.user;

import ee.gaile.entity.enums.EnumRoles;
import ee.gaile.entity.users.Roles;
import ee.gaile.entity.users.Users;
import ee.gaile.service.repository.UserRepository;
import ee.gaile.service.security.LoginService;
import ee.gaile.service.security.UserDetailsImpl;
import ee.gaile.service.security.request.LoginRequest;
import ee.gaile.service.security.request.SignupRequest;
import ee.gaile.service.security.response.MessageResponse;
import ee.gaile.service.security.settings.ApiErrorException;
import ee.gaile.service.security.settings.AuthRefreshDTO;
import ee.gaile.service.security.settings.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class UserService {
    private final LoginService loginService;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public LoginRequest authUser(SignupRequest signupRequest) throws ApiErrorException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signupRequest.getUsername(), signupRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());


        SignupRequest userDTO = SignupRequest.builder()
                .username(signupRequest.getUsername())
                .password(signupRequest.getPassword())
                .role(roles)
                .build();
        return loginService.authUser(userDTO);
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
        Users user = new Users(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<Roles> roles = new HashSet<>();
        Roles userRole = new Roles(EnumRoles.ROLE_USER);
        roles.add(userRole);
        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
