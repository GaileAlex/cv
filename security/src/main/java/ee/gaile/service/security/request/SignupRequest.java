package ee.gaile.service.security.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SignupRequest {

    private String username;

    private String email;

    private String role;

    private String password;

    private String userId;

}
