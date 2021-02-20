package ee.gaile.service.email;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * Service for sending a notification about a site visit
 *
 * @author Aleksei Gaile
 */
@Component
@RequiredArgsConstructor
public class EmailServiceImpl {
    private static final String SUBJECT = "new user";
    private static final String TEXT = "New user noticed from ";
    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String username;

    /**
     * Sends a message about a user visit to the site
     *
     * @param userCountry - user country
     */
    public void sendSimpleMessage(String userCountry) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(username);
        message.setTo(username);
        message.setSubject(SUBJECT);
        message.setText(TEXT + userCountry);
        emailSender.send(message);
    }
}
