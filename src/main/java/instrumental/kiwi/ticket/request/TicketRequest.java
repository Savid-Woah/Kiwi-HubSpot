package instrumental.kiwi.ticket.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static instrumental.kiwi.exception.FieldValidationMessage.FIELD_REQUIRED;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TicketRequest {

    @NotBlank(message = FIELD_REQUIRED)
    private String hsPipeline;

    @NotBlank(message = FIELD_REQUIRED)
    private String hsPipelineStage;

    @NotBlank(message = FIELD_REQUIRED)
    private String hsTicketPriority;

    @NotBlank(message = FIELD_REQUIRED)
    private String subject;

    @NotNull(message = FIELD_REQUIRED)
    private Long orderId;

    @NotNull(message = FIELD_REQUIRED)
    private Long productId;

    @NotNull(message = FIELD_REQUIRED)
    private Integer quantity;
}