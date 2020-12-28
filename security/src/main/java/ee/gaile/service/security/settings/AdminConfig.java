package ee.gaile.service.security.settings;

import ee.gaile.entity.users.Users;
import ee.gaile.enums.EnumRoles;
import ee.gaile.service.security.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class AdminConfig {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    private AdminConfig(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Value("${cv.admin}")
    private String adminName;

    @Value("${cv.admin.email}")
    private String email;

    @Value("${cv.admin.password}")
    private String password;

    @PostConstruct
    private void changeAdmin() {
        Users admin = userRepository.findByRole(EnumRoles.ROLE_ADMIN);
        userRepository.delete(admin);
        Users user = new Users(adminName, email, encoder.encode(password), EnumRoles.ROLE_ADMIN);
        userRepository.save(user);
    }

}
