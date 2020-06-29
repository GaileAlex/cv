package ee.gaile.service.security;

import ee.gaile.service.security.request.LoginRequest;
import ee.gaile.service.security.request.SignupRequest;

import ee.gaile.service.security.settings.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoginService {
    private static final Logger LOG = LoggerFactory.getLogger("can_access_accounting_log");

    private static final String TOKEN_IS_NOT_VALID = "Token is not valid";
    private static final String DEFAULT_LOCALE = "en-EN";
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    public static final String SIGN_IN_URL = "/api/auth/login";
    public static final String SIGN_UP_URL = "/api/auth/signup";
    public static final String AUTH_REFRESH_URL = "/api/auth/refresh";

    public static final String HEADER_STRING = "Authorization";

    @Value("${test-user.username}")
    private String testUsername;
    @Value("${test-user.password}")
    private String testPassword;
    @Value("${security.jwt.key}")
    private String jwtSecureKeyProp;

    private final SuperAdminConfig superAdminConfig;
    private final JWTTokenFactory jwtTokenFactory;

    private static final List<String> ALLOWED_LOCALES = Arrays.asList(
            "en-EN",
            "ru-RU"
    );

    public LoginRequest authUser(SignupRequest auth) throws ApiErrorException {

        if (StringUtils.isEmpty(auth.getUsername()) || StringUtils.isEmpty(auth.getPassword())) {
            throw new ApiErrorException(HttpStatus.FORBIDDEN, ApiErrorType.VALIDATION_ERROR);
        }

        AccountManagementModeParameters accountManagementModeParameters = new AccountManagementModeParameters();

        SignupRequest userDTO = SignupRequest.builder()
                .username(auth.getUsername())
                .password(auth.getPassword())
                .build();

        Claims claims = getJWTClaims(userDTO, accountManagementModeParameters);

        AuthRefreshDTO authRefresh = new AuthRefreshDTO();
        authRefresh.setAccessToken(jwtTokenFactory.createAccessJwtToken(claims));
        authRefresh.setRefreshToken(jwtTokenFactory.createRefreshToken(claims));

        LOG.info("Token was generated for userDTO name={}", userDTO.getUsername());


        return new LoginRequest(authRefresh, userDTO);
    }

    private Claims getJWTClaims(SignupRequest user, AccountManagementModeParameters accountManagementModeParameters) {
        Claims claims = Jwts.claims();
        claims.setSubject(user.getUsername());
        claims.put("roles", "userEntity");
        claims.put("accountManagementMode", accountManagementModeParameters.getAccountManagementMode());

        return claims;
    }


    public AuthRefreshDTO refreshAuth(AuthRefreshDTO authDTO, HttpServletRequest request) throws ApiErrorException {
        try {
            String refreshToken = authDTO.getRefreshToken();
            validateToken(refreshToken, request);
            Claims claims = Jwts.claims();

            authDTO.setAccessToken(jwtTokenFactory.createAccessJwtToken(claims));
            authDTO.setRefreshToken(jwtTokenFactory.createRefreshToken(claims));
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


    public String getUserTokenClaimByPropertyName(String token, String propertyName) throws ServletException {
        try {
            final Claims claims = getTokenClaims(token);
            return claims.get(propertyName) != null ? claims.get(propertyName).toString() : "";
        } catch (final NullPointerException e) {
            throw new ServletException(TOKEN_IS_NOT_VALID, e);
        }
    }

    public String getTestUsername() {
        return testUsername;
    }

    public String getTestPassword() {
        return testPassword;
    }


}
