package instrumental.kiwi.response.handler;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static instrumental.kiwi.response.handler.ResponseHandler.ResponseParameters.*;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResponseHandler {

    static class ResponseParameters {
        public static final String DATA = "data";
        public static final String STATUS = "status";
        public static final String MESSAGE = "message";
    }

    public static Map<String, Object> generateResponse(Object data, HttpStatus status, String message) {

        Map<String, Object> response = new HashMap<>();
        response.put(DATA,  data);
        response.put(STATUS, status);
        response.put(MESSAGE, message);
        return response;
    }

    public static Map<String, Object> generateResponse(HttpStatus status, String message) {

        Map<String, Object> response = new HashMap<>();
        response.put(STATUS, status);
        response.put(MESSAGE, message);
        return response;
    }
}