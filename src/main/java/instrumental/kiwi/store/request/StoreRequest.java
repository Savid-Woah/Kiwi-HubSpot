package instrumental.kiwi.store.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StoreRequest {

    @NotBlank(message = "Field required")
    private String name;

    @NotBlank(message = "Field required")
    private String domain;

    @NotBlank(message = "Field required")
    private String city;

    @NotBlank(message = "Field required")
    private String industry;

    @NotBlank(message = "Field required")
    private String phoneNumber;

    @NotBlank(message = "Field required")
    private String state;

    @NotBlank(message = "Field required")
    private String lifeCycleStage;
}