package ee.gaile.service.user;

import ee.gaile.ApplicationIT;
import ee.gaile.service.security.UserRepository;
import ee.gaile.service.security.request.LoginRequest;
import ee.gaile.service.security.request.SignupRequest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest extends ApplicationIT {

    @Autowired
    UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void authUser() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("Admin");

        SignupRequest signupRequest = SignupRequest.builder()
                .username("Admin")
                .role("ROLE_ADMIN")
                .password("admin+")
                .email("admin@cv.ee")
                .build();

        LoginRequest loginRequest = userService.authUser(signupRequest);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(loginRequest.getUser().getUsername()).isEqualTo("Admin");
            softly.assertThat(loginRequest.getUser().getRole()).isEqualTo("ROLE_ADMIN");
            softly.assertThat(loginRequest.getUser().getPassword()).isEqualTo("admin+");
            softly.assertThat(loginRequest.getAuth().getTokenType()).isEqualTo("Bearer");
        });
    }

    @Test
    void registerUser() {
        SignupRequest signupRequest = SignupRequest.builder()
                .username("TestUser")
                .password("TestUser")
                .email("test@test.ee")
                .build();

        ResponseEntity<?> responseEntity = userService.registerUser(signupRequest);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        });

        userRepository.delete(userRepository.findByUsername("TestUser").get());
    }
}
