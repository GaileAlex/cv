package ee.gaile.service.user.impl;

import ee.gaile.entity.statistics.VisitStatisticsEntity;
import ee.gaile.entity.users.UserEntity;
import ee.gaile.enums.EnumRoles;
import ee.gaile.repository.statistic.VisitStatisticsRepository;
import ee.gaile.service.security.LoginService;
import ee.gaile.service.security.UserDetailsImpl;
import ee.gaile.service.security.UserRepository;
import ee.gaile.service.security.settings.LoginRequest;
import ee.gaile.models.auth.request.SignupRequest;
import ee.gaile.models.auth.response.MessageResponse;
import ee.gaile.apiexeption.ApiErrorException;
import ee.gaile.service.security.settings.AuthRefreshDTO;
import ee.gaile.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * User access service
 *
 * @author Aleksei Gaile
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final LoginService loginService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final VisitStatisticsRepository visitStatisticsRepository;

    @Override
    public LoginRequest authUser(SignupRequest signupRequest, HttpServletRequest request) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signupRequest.getUsername(), signupRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid user data");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).distinct().collect(Collectors.toList());

        SignupRequest userDTO = SignupRequest.builder()
                .username(signupRequest.getUsername())
                .password(signupRequest.getPassword())
                .role(role.get(0))
                .build();

        Optional<VisitStatisticsEntity> visitStatistics = visitStatisticsRepository.findBySessionId(request.getHeader("userId"));
        visitStatistics.ifPresent(c -> visitStatisticsRepository.save(c.setUsername(signupRequest.getUsername())));

        return loginService.authUser(userDTO);
    }

    @Override
    public AuthRefreshDTO refreshAuth(AuthRefreshDTO authDTO, HttpServletRequest request) throws ApiErrorException {
        return loginService.refreshAuth(authDTO, request);
    }

    @Override
    public ResponseEntity<MessageResponse> registerUser(SignupRequest signUpRequest) {
        if (Boolean.TRUE.equals(userRepository.existsByUsername(signUpRequest.getUsername())) ||
                signUpRequest.getUsername().trim().equals("undefined")) {

            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }
        if (Boolean.TRUE.equals(userRepository.existsByEmail(signUpRequest.getEmail()))) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        userRepository.save(new UserEntity(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword()), EnumRoles.ROLE_USER));

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

}
