package ee.gaile.service.security.settings;

import ee.gaile.enums.EnumRoles;
import ee.gaile.entity.users.Users;
import ee.gaile.service.security.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;

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

    @Value("${cv.user}")
    private String testUser;

    @Value("${cv.user.email}")
    private String testUserEmail;

    @Value("${cv.user.password}")
    private String testUserPassword;

    @PostConstruct
    private void changeAdmin() {
        Users adminRepo = userRepository.findByRole(EnumRoles.ROLE_ADMIN);
        if (adminRepo == null) {
            saveUser(admin, email, password, EnumRoles.ROLE_ADMIN);
        } else if (!adminRepo.getUsername().equals(admin)) {
            userRepository.deleteById(adminRepo.getId());
            saveUser(admin, email, password, EnumRoles.ROLE_ADMIN);
        }
    }

    @PostConstruct
    private void changeTestUser() {
        Optional<Users> userRepo = userRepository.findByUsername(testUser);
        if (!userRepo.isPresent()) {
            saveUser(testUser, testUserEmail, testUserPassword, EnumRoles.ROLE_USER);
        }
    }

    private void saveUser(String name, String email, String password, EnumRoles role) {
        Users user = new Users(name, email, encoder.encode(password), role);
        userRepository.save(user);
    }

}
