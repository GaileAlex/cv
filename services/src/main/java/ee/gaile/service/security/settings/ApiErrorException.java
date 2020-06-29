package ee.gaile.service.security.settings;

import org.springframework.http.HttpStatus;

public class ApiErrorException extends Exception {

    private HttpStatus status;
    private String message;
    private String code;

    public ApiErrorException(HttpStatus status, ApiErrorType error) {
        super();
        this.status = status;
        this.message = error.getMessage();
        this.code = error.getCode();
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
