package ee.gaile.service.security.settings;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiErrorUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String convertErrorToJSON(ApiErrorException error) throws IOException {
        return convertErrorToJSON(error.getCode(), error.getMessage());
    }

    public static String convertErrorToJSON(ApiErrorType error) throws IOException {
        return convertErrorToJSON(error.getCode(), error.getMessage());
    }

    public static String convertErrorToJSON(String code, String message) throws IOException {
        Map<String, Object> errorObj = new HashMap<>();
        errorObj.put("code", code);
        errorObj.put("message", message);
        return objectMapper.writeValueAsString(errorObj);
    }

}
