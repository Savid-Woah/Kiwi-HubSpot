package instrumental.kiwi.product.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import instrumental.kiwi.stock.request.StockData;
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
public class ProductRequest {

    @NotBlank(message = FIELD_REQUIRED)
    private String name;

    @NotNull(message = FIELD_REQUIRED)
    private Double price;

    @NotBlank(message = FIELD_REQUIRED)
    private String hsSku;

    @NotBlank(message = FIELD_REQUIRED)
    private String description;

    @NotBlank(message = FIELD_REQUIRED)
    private String hsCostOfGoodsSold;

    @NotBlank(message = FIELD_REQUIRED)
    private String hsRecurringBillingPeriod;

    @NotNull(message = FIELD_REQUIRED)
    private StockData stockData;

    @NotNull(message = FIELD_REQUIRED)
    private Long storeId;

    @NotNull(message = FIELD_REQUIRED)
    private Long contactId;
}