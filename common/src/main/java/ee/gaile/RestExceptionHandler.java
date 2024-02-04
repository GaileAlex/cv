package ee.gaile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
@AllArgsConstructor
public class RestExceptionHandler {
    private static final String MESSAGE = "message";
    private static final Logger LOG = LoggerFactory.getLogger("error-log");

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleException(Exception e) {
        String message = e.getMessage();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        LOG.error("The following error occurred: message - {}, exception - {}", message, e);

        if (e instanceof ResponseStatusException ex) {
            objectNode.put(MESSAGE, (ex).getReason());
            return ResponseEntity.status((ex).getStatusCode())
                    .body(objectNode);
        }

        objectNode.put(MESSAGE, message);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(objectNode);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<Object> handleException(HttpRequestMethodNotSupportedException e) {
        String message = e.getMessage();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        LOG.error("the following error occurred {}", message);

        objectNode.put(MESSAGE, message);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(objectNode);
    }

}
