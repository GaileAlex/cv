package ee.gaile.service.security.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignupRequest {
    private String username;

    private String email;

    private String role;

    private String password;

    private String userId;

}
