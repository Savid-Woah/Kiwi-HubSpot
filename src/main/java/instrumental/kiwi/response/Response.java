package instrumental.kiwi.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Response implements Serializable {

    private Object data;
    private String message;
    private LocalDateTime at;
    private HttpStatus status;

    public Response(Object data, String message, HttpStatus status) {

        this.at = now();
        this.data = data;
        this.status = status;
        this.message = message;
    }

    public Response(HttpStatus status, String message) {

        this.at = now();
        this.status = status;
        this.message = message;
    }
}