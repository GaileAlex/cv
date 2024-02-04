package ee.gaile.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.RequestContextFilter;

import java.io.IOException;

/**
 * @author Aleksei Gaile 28-May-22
 */
@Component
public class FilterConfig extends RequestContextFilter {

    private static final String ADMIN_ID = "b26ab859-a676-4372-811c-da5c98e3a213";

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                 FilterChain filterChain)
            throws IOException, ServletException {
        String userId = request.getHeader("userId");
        if (ADMIN_ID.equals(userId) && !request.getRequestURI().equals("/api/auth/login")) {
            return;
        }

        filterChain.doFilter(request, response);
    }

}
