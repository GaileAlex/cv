package ee.gaile.service.security.request;

import ee.gaile.service.security.settings.AuthRefreshDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LoginRequest {

    private AuthRefreshDTO auth;

    private SignupRequest user;

}
