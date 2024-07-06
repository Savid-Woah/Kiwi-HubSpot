package instrumental.kiwi.line_item.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static instrumental.kiwi.exception.FieldValidationMessage.FIELD_REQUIRED;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssociationType {

    @NotBlank(message = FIELD_REQUIRED)
    private String associationCategory;

    @NotBlank(message = FIELD_REQUIRED)
    private Integer associationTypeId;
}