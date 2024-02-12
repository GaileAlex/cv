package ee.gaile.apiexeption;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiErrorException extends RuntimeException {

    private final HttpStatus status;
    private final String message;
    private final String code;

    public ApiErrorException(HttpStatus status, ApiErrorType error) {
        super();
        this.status = status;
        this.message = error.getMessage();
        this.code = error.getCode();
    }

}
