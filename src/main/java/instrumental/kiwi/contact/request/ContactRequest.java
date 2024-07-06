package instrumental.kiwi.contact.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
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
public class ContactRequest {

    @NotBlank(message = "Field required")
    private String email;

    @NotBlank(message = "Field required")
    private String firstname;

    @NotBlank(message = "Field required")
    private String lastname;

    @NotBlank(message = "Field required")
    private String phone;

    @NotBlank(message = "Field required")
    private String company;

    @NotBlank(message = "Field required")
    private String website;

    @NotBlank(message = "Field required")
    private String lifecyclestage;

    @NotNull(message = "Field required")
    private Long storeId;
}