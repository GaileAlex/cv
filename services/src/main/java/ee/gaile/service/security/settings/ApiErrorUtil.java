package ee.gaile.service.security.settings;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ApiErrorUtil {

    static private ObjectMapper objectMapper = new ObjectMapper();

    static public String convertErrorToJSON(ApiErrorException error) throws IOException {
        return convertErrorToJSON(error.getCode(), error.getMessage());
    }

    static public String convertErrorToJSON(ApiErrorType error) throws IOException {
        return convertErrorToJSON(error.getCode(), error.getMessage());
    }

    static public String convertErrorToJSON(String code, String message) throws IOException {
        Map<String, Object> errorObj = new HashMap<>();
        errorObj.put("code", code);
        errorObj.put("message", message);
        return objectMapper.writeValueAsString(errorObj);
    }
}
