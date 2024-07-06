package instrumental.kiwi.stock.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import static instrumental.kiwi.exception.FieldValidationMessage.FIELD_REQUIRED;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StockRequest {

    @NotNull(message = FIELD_REQUIRED)
    private UUID productId;

    @NotNull(message = FIELD_REQUIRED)
    private Integer quantity;

    @NotNull(message = FIELD_REQUIRED)
    private Integer notificationTriggeringQuantity;
}