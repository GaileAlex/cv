package ee.gaile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
@AllArgsConstructor
public class RestExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger("error-log");

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleException(Exception e) {
        String message = e.getMessage();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        LOG.error("rest error occurred \n " + message, e);

        if (e instanceof ResponseStatusException) {
            objectNode.put("error", ((ResponseStatusException) e).getReason());
            return ResponseEntity.status(((ResponseStatusException) e).getStatus())
                    .body(objectNode);
        }

        objectNode.put("error", message);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(objectNode);
    }

}
