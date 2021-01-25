package ee.gaile.service.user;

import ee.gaile.entity.statistics.VisitStatistics;
import ee.gaile.entity.users.Users;
import ee.gaile.repository.statistic.VisitStatisticsRepository;
import ee.gaile.service.security.LoginService;
import ee.gaile.service.security.UserDetailsImpl;
import ee.gaile.service.security.UserRepository;
import ee.gaile.service.security.request.LoginRequest;
import ee.gaile.service.security.request.SignupRequest;
import ee.gaile.service.security.response.MessageResponse;
import ee.gaile.service.security.settings.ApiErrorException;
import ee.gaile.service.security.settings.AuthRefreshDTO;
import ee.gaile.enums.EnumRoles;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class UserService {
    private final LoginService loginService;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final VisitStatisticsRepository visitStatisticsRepository;

    public LoginRequest authUser(SignupRequest signupRequest, HttpServletRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signupRequest.getUsername(), signupRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).distinct().collect(Collectors.toList());

        SignupRequest userDTO = SignupRequest.builder()
                .username(signupRequest.getUsername())
                .password(signupRequest.getPassword())
                .role(role.get(0))
                .build();

        Optional<VisitStatistics> visitStatistics = visitStatisticsRepository.findBySessionId(request.getHeader("userId"));
        visitStatistics.ifPresent((c) -> visitStatisticsRepository.save(c.setUsername(signupRequest.getUsername())));

        return loginService.authUser(userDTO);
    }

    public AuthRefreshDTO refreshAuth(AuthRefreshDTO authDTO, HttpServletRequest request) throws ApiErrorException {
        return loginService.refreshAuth(authDTO, request);
    }

    public ResponseEntity<?> registerUser(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername()) ||
                signUpRequest.getUsername().trim().equals("undefined")) {

            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        userRepository.save(new Users(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()), EnumRoles.ROLE_USER));

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

}
