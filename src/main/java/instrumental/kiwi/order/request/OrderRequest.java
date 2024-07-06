package instrumental.kiwi.order.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import instrumental.kiwi.line_item.request.ItemPurchased;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static instrumental.kiwi.exception.FieldValidationError.FIELD_REQUIRED;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderRequest {

    @NotBlank(message = FIELD_REQUIRED)
    private String hsOrderName;

    @NotBlank(message = FIELD_REQUIRED)
    private String hsCurrencyCode;

    @NotBlank(message = FIELD_REQUIRED)
    private String hsSourceStore;

    @NotBlank(message = FIELD_REQUIRED)
    private String hsFulfillmentStatus;

    @NotBlank(message = FIELD_REQUIRED)
    private String hsShippingAddressCity;

    @NotBlank(message = FIELD_REQUIRED)
    private String hsShippingAddressState;

    @NotBlank(message = FIELD_REQUIRED)
    private String hsShippingAddressStreet;

    @NotNull(message = FIELD_REQUIRED)
    private List<ItemPurchased> itemsPurchased;

    @NotNull(message = FIELD_REQUIRED)
    private Long customerId;
}
