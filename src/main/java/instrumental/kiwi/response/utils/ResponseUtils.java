package instrumental.kiwi.response.utils;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import instrumental.kiwi.response.Response;
import org.springframework.http.HttpStatus;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResponseUtils {

    public static Response generateResponse(Object data, HttpStatus status, String message) {
        return new Response(data, message, status);
    }

    public static Response generateResponse(HttpStatus status, String message) {
        return new Response(status, message);
    }
}