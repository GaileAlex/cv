package ee.gaile;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;


@ControllerAdvice
@RequiredArgsConstructor
public class RestExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger("error-log");

    @Autowired
    private Environment environment;

    @ExceptionHandler(Exception.class)
    protected ResponseEntity handleException(Exception e) throws Exception {
        LOG.error("rest error occurred \n " + e.getMessage(), e);
        if (Arrays.asList(environment.getActiveProfiles()).contains("prod")) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("internal server error");
        } else {
            throw e;
        }
    }

}
