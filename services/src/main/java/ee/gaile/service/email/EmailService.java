package ee.gaile.service.email;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Aleksei Gaile 18-Sep-21
 */
public interface EmailService {

    void sendSimpleMessage(HttpServletRequest request);

}
