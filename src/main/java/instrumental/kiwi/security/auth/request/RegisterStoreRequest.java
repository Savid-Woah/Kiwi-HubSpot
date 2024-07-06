package instrumental.kiwi.security.auth.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import instrumental.kiwi.store.request.StoreRequest;
import instrumental.kiwi.security.user.request.UserRequest;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static instrumental.kiwi.exception.FieldValidationError.FIELD_REQUIRED;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RegisterStoreRequest {

    @NotNull(message = FIELD_REQUIRED)
    private UserRequest userRequest;

    @NotNull(message = FIELD_REQUIRED)
    private StoreRequest storeRequest;
}