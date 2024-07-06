package instrumental.kiwi.line_item.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import instrumental.kiwi.product.model.Product;
import jakarta.validation.constraints.NotBlank;
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
public class LineItemRequest {

    @NotBlank(message = FIELD_REQUIRED)
    private String name;

    @NotNull(message = FIELD_REQUIRED)
    private Integer quantity;

    @NotBlank(message = FIELD_REQUIRED)
    private String hsProductId;

    public LineItemRequest(Product product, Integer quantity) {

        this.name = product.getName();
        this.quantity = quantity;
        this.hsProductId = product.getProductId().toString();
    }
}
