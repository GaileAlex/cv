package ee.gaile.service.security.settings;

import lombok.Data;

import static ee.gaile.service.security.settings.JwtUtils.TOKEN_TYPE;

@Data
public class AuthRefreshDTO {

    String tokenType = TOKEN_TYPE;

    String accessToken;

    String refreshToken;

}
