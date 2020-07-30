package ee.gaile.service.security;

import ee.gaile.service.security.request.LoginRequest;
import ee.gaile.service.security.request.SignupRequest;
import ee.gaile.service.security.settings.ApiErrorException;
import ee.gaile.service.security.settings.ApiErrorType;
import ee.gaile.service.security.settings.AuthRefreshDTO;
import ee.gaile.service.security.settings.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class LoginService {
    private static final Logger ACCESS_LOG = LoggerFactory.getLogger("access-accounting-log");

    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    public static final String LOGIN_URL = "/api/auth/login";
    public static final String LOGOUT_URL = "/api/auth/logout";
    public static final String AUTH_REFRESH_URL = "/api/auth/refresh";

    public static final String HEADER_STRING = "Authorization";

    @Value("${security.jwt.key}")
    private String jwtSecureKeyProp;

    private final JwtUtils jwtUtils;

    public LoginRequest authUser(SignupRequest userDTO) {
        Claims claims = getJWTClaims(userDTO);

        AuthRefreshDTO authRefresh = new AuthRefreshDTO();
        authRefresh.setAccessToken(jwtUtils.generateJwtToken(claims));
        authRefresh.setRefreshToken(jwtUtils.createRefreshToken(claims));

        ACCESS_LOG.info("Token was generated for userDTO name={}", userDTO.getUsername());

        return new LoginRequest(authRefresh, userDTO);
    }

    private Claims getJWTClaims(SignupRequest user) {
        Claims claims = Jwts.claims();
        claims.setSubject(user.getUsername());
        claims.put("roles", user.getRole());

        return claims;
    }


    public AuthRefreshDTO refreshAuth(AuthRefreshDTO authDTO, HttpServletRequest request) throws ApiErrorException {
        try {
            String refreshToken = authDTO.getRefreshToken();
            validateToken(refreshToken, request);
            Claims claims = Jwts.claims();

            authDTO.setAccessToken(jwtUtils.generateJwtToken(claims));
            authDTO.setRefreshToken(jwtUtils.createRefreshToken(claims));
            return authDTO;
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            throw new ApiErrorException(HttpStatus.FORBIDDEN, ApiErrorType.REFRESH_TOKEN_EXPIRED);
        } catch (Exception e) {
            throw new ApiErrorException(HttpStatus.FORBIDDEN, ApiErrorType.INVALID_REFRESH_TOKEN);
        }
    }

    public UsernamePasswordAuthenticationToken validateToken(String token,
                                                             HttpServletRequest request) {
        final Claims claims = getTokenClaims(token);
        request.setAttribute("claims", claims);
        return new UsernamePasswordAuthenticationToken(
                claims.getSubject(),
                null,
                new ArrayList<>());
    }

    private Claims getTokenClaims(String token) {
        final byte[] secretBytes = jwtSecureKeyProp.getBytes();
        final Key signingKey = new SecretKeySpec(secretBytes, SIGNATURE_ALGORITHM.getJcaName());
        return Jwts.parser()
                .setSigningKey(signingKey)
                .parseClaimsJws(token)
                .getBody();
    }
}
