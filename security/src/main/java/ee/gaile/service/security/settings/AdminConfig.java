package ee.gaile.service.security.settings;

import ee.gaile.entity.users.UserEntity;
import ee.gaile.enums.EnumRoles;
import ee.gaile.service.security.SecurityConfig;
import ee.gaile.service.security.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class AdminConfig {
    private final UserRepository userRepository;
    private final SecurityConfig securityConfig;

    @Value("${cv.admin}")
    private String adminName;

    @Value("${cv.admin.email}")
    private String email;

    @Value("${cv.admin.password}")
    private String password;

    @PostConstruct
    private void changeAdmin() {
        UserEntity admin = userRepository.findByRole(EnumRoles.ROLE_ADMIN);
        if (admin == null || !admin.getUsername().equals(adminName) || !admin.getEmail().equals(email)
                || !securityConfig.passwordEncoder()
                .matches(password, admin.getPassword())) {

            if (admin != null) {
                userRepository.delete(admin);
            }
            UserEntity user = new UserEntity(adminName, email, securityConfig.passwordEncoder().encode(password), EnumRoles.ROLE_ADMIN);
            userRepository.save(user);
        }
    }

}
