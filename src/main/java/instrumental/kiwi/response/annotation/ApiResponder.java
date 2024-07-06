package instrumental.kiwi.response.annotation;

import instrumental.kiwi.exception.Error;
import instrumental.kiwi.exception.FieldValidationError;
import instrumental.kiwi.response.Response;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Successful operation",
                content = @Content(schema = @Schema(implementation = Response.class))),
        @ApiResponse(
                responseCode = "400",
                description = "Bad request",
                content = @Content(schema = @Schema(implementation = FieldValidationError.class))
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(schema = @Schema(implementation = Error.class)))
})
public @interface ApiResponder {
}
