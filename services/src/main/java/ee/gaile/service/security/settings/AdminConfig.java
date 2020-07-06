package ee.gaile.service.security.settings;

import ee.gaile.entity.enums.EnumRoles;
import ee.gaile.entity.users.Users;
import ee.gaile.service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AdminConfig implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public AdminConfig(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Value("${cv.admin}")
    private String admin;

    @Value("${cv.admin.password}")
    private String password;

    @Override
    public void run(String... args) {
        if (userRepository.findByRole(EnumRoles.ROLE_ADMIN)==null) {
            Users user = new Users(admin, "admin@cv.ee", encoder.encode(password), EnumRoles.ROLE_ADMIN);
            userRepository.save(user);
        }
    }
}
