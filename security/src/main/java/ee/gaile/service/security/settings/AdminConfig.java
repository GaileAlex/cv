package ee.gaile.service.security.settings;

import ee.gaile.entity.enums.EnumRoles;
import ee.gaile.entity.users.Users;
import ee.gaile.service.security.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminConfig {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    private AdminConfig(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Value("${cv.admin}")
    private String admin;

    @Value("${cv.admin.email}")
    private String email;

    @Value("${cv.admin.password}")
    private String password;

    @Bean
    private void changeAdmin() {
        Users adminRepo = userRepository.findByRole(EnumRoles.ROLE_ADMIN);
        if (adminRepo.getUsername() == null) {
            saveAdmin();
        } else if (!adminRepo.getUsername().equals(admin)) {
            userRepository.deleteById(adminRepo.getId());
            saveAdmin();
        }
    }

    private void saveAdmin() {
        Users user = new Users(admin, email, encoder.encode(password), EnumRoles.ROLE_ADMIN);
        userRepository.save(user);
    }
}
