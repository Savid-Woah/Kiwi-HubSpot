package instrumental.kiwi.customer.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
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
public class CustomerRequest {

    @NotBlank(message = FIELD_REQUIRED)
    private String name;

    @NotBlank(message = FIELD_REQUIRED)
    private String lastName;

    @NotBlank(message = FIELD_REQUIRED)
    private String phoneNumber;

    @NotBlank(message = FIELD_REQUIRED)
    private Long storeId;
}