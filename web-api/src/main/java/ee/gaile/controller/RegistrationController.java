/*
package ee.gaile.controller;

import ee.gaile.configuration.security.RegistrationForm;
import ee.gaile.repository.blog.UserRepository;
import ee.gaile.repository.entity.blog.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/register")
@CrossOrigin(exposedHeaders="Access-Control-Allow-Origin")
public class RegistrationController {
    private UserRepository userRepo;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationController(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String registerForm() {
        return "registration";
    }

    @PostMapping
    public String processRegistration(RegistrationForm form, Model model) {
        String mistakeValid;
        List<User> userList = userRepo.findAll();

        for (User user : userList) {
            if (user.getUsername().equals(form.getUsername())) {
                mistakeValid = "This name is already taken";
                model.addAttribute("mistakeValid", mistakeValid);
                return "registration";
            }
            if (user.getEmail().equals(form.getEmail())) {
                mistakeValid = "This email already exists.";
                model.addAttribute("mistakeValid", mistakeValid);
                return "registration";
            }
        }
        userRepo.save(form.toUser(passwordEncoder));
        return "redirect:/login";
    }
}
*/