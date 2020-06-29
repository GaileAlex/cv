package ee.gaile.service.security.settings;

public enum ApiErrorType {

    ACCESS_TOKEN_EXPIRED("access_token_expired", "Access token expired"),
    REFRESH_TOKEN_EXPIRED("refresh_token_expired", "Refresh token expired"),
    INVALID_CREDENTIALS("invalid_credentials", "Invalid credentials"),
    VALIDATION_ERROR("validation_error", "Validation error"),
    INVALID_ACCESS_TOKEN("invalid_access_token", "Invalid access token"),
    INVALID_REFRESH_TOKEN("invalid_refresh_token", "Invalid refresh token"),
    NOT_ALLOWED_FOR_THIS_PARTNER("not_allowed_fot_this_partner", "You are not allowed to change settings for this partner"),
    DATA_ACCESS_EXCEPTION("data_access_exception", "Data access exception");

    private String code;
    private String message;

    ApiErrorType(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
