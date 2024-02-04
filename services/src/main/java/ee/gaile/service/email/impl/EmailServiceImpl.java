package ee.gaile.service.email.impl;

import ee.gaile.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Service for sending a notification about a site visit
 *
 * @author Aleksei Gaile
 */
@Component
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private static final String SUBJECT = "new user";
    private static final String TEXT_COUNTRY = "New user noticed from ";
    private static final String TEXT_ID = " user ID - ";
    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String username;

    /**
     * Sends a message about a user visit to the site
     *
     * @param request - user country, ID
     */
    public void sendSimpleMessage(HttpServletRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(username);
        message.setTo(username);
        message.setSubject(SUBJECT);
        message.setText(TEXT_COUNTRY + request.getHeader("userCountry") + TEXT_ID + request.getHeader("userId"));
        emailSender.send(message);
    }
}
