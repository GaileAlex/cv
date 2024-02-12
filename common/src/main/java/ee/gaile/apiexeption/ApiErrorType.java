package ee.gaile.apiexeption;

import lombok.Getter;

@Getter
public enum ApiErrorType {

    ACCESS_TOKEN_EXPIRED("access_token_expired", "Access token expired"),
    REFRESH_TOKEN_EXPIRED("refresh_token_expired", "Refresh token expired"),
    INVALID_USERNAME("invalid_username", "Invalid username"),
    INVALID_PASSWORD("invalid_password", "Invalid password"),
    INVALID_ACCESS_TOKEN("invalid_access_token", "Invalid access token"),
    INVALID_REFRESH_TOKEN("invalid_refresh_token", "Invalid refresh token");

    private final String code;
    private final String message;

    ApiErrorType(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
