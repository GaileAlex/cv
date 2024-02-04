package ee.gaile.service.security.settings;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtils {
    @Value("${security.jwt.key}")
    private String jwtSecret;

    @Value("${security.jwt.token.live.minutes}")
    private int jwtExpiration;

    public static final String TOKEN_TYPE = "Bearer";
    public static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    public String generateJwtToken(Claims claims) {
        if (StringUtils.isBlank(claims.getSubject()))
            throw new IllegalArgumentException("Cannot create JWT Token without subject");

        // Current time
        LocalDateTime currentTime = LocalDateTime.now(ZoneId.systemDefault());

        // Get expiration data
        LocalDateTime expDateTime = currentTime.plusMinutes(jwtExpiration);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(claims.getSubject())
                .setIssuedAt(getDateWithZone(currentTime))
                .setExpiration(getDateWithZone(expDateTime))
                .signWith(SIGNATURE_ALGORITHM, getSigningKey())
                .compact();
    }

    public String createRefreshToken(Claims claims) {
        if (StringUtils.isBlank(claims.getSubject()))
            throw new IllegalArgumentException("Cannot create JWT Token without subject");

        claims.put("scopes", Collections.singletonList("ROLE_REFRESH_TOKEN"));

        // Current time
        LocalDateTime currentTime = LocalDateTime.now(ZoneId.systemDefault());

        // Get expiration data
        LocalDateTime expDateTime = currentTime.plusMinutes(jwtExpiration);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(claims.getSubject())
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(getDateWithZone(currentTime))
                .setExpiration(getDateWithZone(expDateTime))
                .signWith(SIGNATURE_ALGORITHM, getSigningKey())
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    private Key getSigningKey() {
        final byte[] secretBytes = jwtSecret.getBytes();
        return new SecretKeySpec(secretBytes, SIGNATURE_ALGORITHM.getJcaName());
    }

    private Date getDateWithZone(LocalDateTime time) {
        return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }
}
