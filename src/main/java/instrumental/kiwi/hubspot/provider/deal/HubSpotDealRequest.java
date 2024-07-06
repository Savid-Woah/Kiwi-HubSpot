package instrumental.kiwi.hubspot.provider.deal;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static instrumental.kiwi.exception.FieldValidationMessage.FIELD_REQUIRED;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class HubSpotDealRequest {

    @NotNull(message = FIELD_REQUIRED)
    private String amount;

    @NotNull(message = FIELD_REQUIRED)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime closedate;

    @NotBlank(message = FIELD_REQUIRED)
    private String dealname;

    @NotBlank(message = FIELD_REQUIRED)
    private String pipeline;

    @NotBlank(message = FIELD_REQUIRED)
    private String dealstage;

    @NotBlank(message = FIELD_REQUIRED)
    private String companyId;

    public HubSpotDealRequest(String amount, String companyId) {

        this.amount = amount;
        this.dealname = "Order";
        this.pipeline = "default";
        this.dealstage = "finished";
        this.companyId = companyId;
        this.closedate = LocalDateTime.now();
    }
}
