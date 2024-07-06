package instrumental.kiwi.security.auth.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import instrumental.kiwi.customer.request.CustomerRequest;
import instrumental.kiwi.security.user.request.UserRequest;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RegisterCustomerRequest {

    @NotNull(message = "Field required")
    private UserRequest userRequest;

    @NotNull(message = "Field required")
    private CustomerRequest customerRequest;
}