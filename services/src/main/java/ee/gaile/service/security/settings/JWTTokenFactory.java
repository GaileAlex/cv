package ee.gaile.service.security.settings;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

@Component
public class JWTTokenFactory {

    @Value("${security.jwt.key}")
    private String jwtSecureKeyProp;

    @Value("${security.jwt.token.live.minutes}")
    public Long JWT_TOKEN_LIVE_PROP;
    @Value("${security.jwt.refreshToken.live.minutes}")
    public Long JWT_REFRESH_TOKEN_LIVE_PROP;

    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
    public static final String TOKEN_TYPE = "Bearer";

    @Autowired
    private Environment env;

    /**
     * Create new access token
     * @param claims
     * @return
     */
    public String createAccessJwtToken(Claims claims) {
        if (StringUtils.isBlank(claims.getSubject()))
            throw new IllegalArgumentException("Cannot create JWT Token without subject");

        // Current time
        LocalDateTime currentTime =  LocalDateTime.now(ZoneId.systemDefault());

        // Get expiration data
        LocalDateTime expDateTime = currentTime.plusMinutes(JWT_TOKEN_LIVE_PROP);

        String jwtToken = Jwts.builder()
                .setClaims(claims)
                .setIssuer(claims.getSubject())
                .setIssuedAt(getDateWithZone(currentTime))
                .setExpiration(getDateWithZone(expDateTime))
                .signWith(SIGNATURE_ALGORITHM, getSigningKey())
                .compact();

        return jwtToken;
    }

    /**
     * Create new refresh token
     * @param claims
     * @return
     */
    public String createRefreshToken(Claims claims) {
        if (StringUtils.isBlank(claims.getSubject()))
            throw new IllegalArgumentException("Cannot create JWT Token without subject");

        claims.put("scopes", Collections.singletonList("ROLE_REFRESH_TOKEN"));

        // Current time
        LocalDateTime currentTime =  LocalDateTime.now(ZoneId.systemDefault());

        // Get expiration data
        LocalDateTime expDateTime = currentTime.plusMinutes(JWT_REFRESH_TOKEN_LIVE_PROP);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(claims.getSubject())
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(getDateWithZone(currentTime))
                .setExpiration(getDateWithZone(expDateTime))
                .signWith(SIGNATURE_ALGORITHM, getSigningKey())
                .compact();
    }

    private Key getSigningKey() {
        final byte[] secretBytes = jwtSecureKeyProp.getBytes();
        return new SecretKeySpec(secretBytes, SIGNATURE_ALGORITHM.getJcaName());
    }

    private Date getDateWithZone(LocalDateTime time) {
        return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }
}
