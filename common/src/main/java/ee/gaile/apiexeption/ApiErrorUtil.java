package ee.gaile.apiexeption;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiErrorUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Converts ApiErrorException to JSON format.
     *
     * @param  error  the ApiErrorException to be converted
     * @return       the JSON string representing the ApiErrorException
     */
    public static String convertErrorToJSON(ApiErrorException error) throws IOException {
        return convertErrorToJSON(error.getCode(), error.getMessage());
    }

    /**
     * Convert the given ApiErrorType to JSON format.
     *
     * @param  error  the ApiErrorType to be converted
     * @return          the JSON representation of the ApiErrorType
     */
    public static String convertErrorToJSON(ApiErrorType error) throws IOException {
        return convertErrorToJSON(error.getCode(), error.getMessage());
    }

    /**
     * Converts an error message and code into a JSON string.
     *
     * @param  code    the error code
     * @param  message the error message
     * @return         the JSON string representing the error object
     */
    public static String convertErrorToJSON(String code, String message) throws IOException {
        Map<String, Object> errorObj = new HashMap<>();
        errorObj.put("code", code);
        errorObj.put("message", message);
        return objectMapper.writeValueAsString(errorObj);
    }

}
