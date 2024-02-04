package ee.gaile.service.security;


import ee.gaile.service.security.settings.ApiErrorType;
import ee.gaile.service.security.settings.ApiErrorUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

import static ee.gaile.service.security.LoginService.HEADER_STRING;
import static ee.gaile.service.security.settings.JwtUtils.TOKEN_TYPE;

@Slf4j
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    private final LoginService loginService;

    public JWTAuthorizationFilter(AuthenticationManager authManager, LoginService loginService) {
        super(authManager);
        this.loginService = loginService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(HEADER_STRING);
        if (header == null || !header.startsWith(TOKEN_TYPE)) {
            chain.doFilter(req, res);
            return;
        }
        UsernamePasswordAuthenticationToken authentication;
        try {
            authentication = loginService.validateToken(header.substring(TOKEN_TYPE.length() + 1), req);
        } catch (ExpiredJwtException e) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setContentType("application/json");
            res.getOutputStream().println(ApiErrorUtil.convertErrorToJSON(ApiErrorType.ACCESS_TOKEN_EXPIRED));
            return;
        } catch (Exception e) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setContentType("application/json");
            res.getOutputStream().println(ApiErrorUtil.convertErrorToJSON(ApiErrorType.INVALID_ACCESS_TOKEN));
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

}
