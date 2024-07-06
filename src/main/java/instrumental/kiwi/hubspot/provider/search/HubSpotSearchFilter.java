package instrumental.kiwi.hubspot.provider.search;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static instrumental.kiwi.exception.FieldValidationMessage.FIELD_REQUIRED;
import static instrumental.kiwi.hubspot.param.HubSpotProductParam.COMPANY_ID;
import static instrumental.kiwi.hubspot.param.HubSpotTicketParam.ORDER_ID;
import static instrumental.kiwi.hubspot.provider.search.HubSpotSearchOperator.EQUAL;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HubSpotSearchFilter {

    @NotBlank(message = FIELD_REQUIRED)
    private String propertyName;

    @NotBlank(message = FIELD_REQUIRED)
    private String operator;

    @NotBlank(message = FIELD_REQUIRED)
    private String value;

    public static HubSpotSearchFilter buildFilterForProductsByStore(Long storeId) {

        HubSpotSearchFilter filter = new HubSpotSearchFilter();
        filter.setPropertyName(COMPANY_ID);
        filter.setValue(storeId.toString());
        filter.setOperator(EQUAL);
        return filter;
    }

    public static HubSpotSearchFilter buildFilterForContactsByStore(Long storeId) {

        HubSpotSearchFilter filter = new HubSpotSearchFilter();
        filter.setPropertyName(COMPANY_ID);
        filter.setValue(storeId.toString());
        filter.setOperator(EQUAL);
        return filter;
    }

    public static HubSpotSearchFilter buildFilterForOrdersByStore(Long storeId) {

        HubSpotSearchFilter filter = new HubSpotSearchFilter();
        filter.setPropertyName(COMPANY_ID);
        filter.setValue(storeId.toString());
        filter.setOperator(EQUAL);
        return filter;
    }

    public static HubSpotSearchFilter buildFilterForDealsByStore(Long storeId) {

        HubSpotSearchFilter filter = new HubSpotSearchFilter();
        filter.setPropertyName(COMPANY_ID);
        filter.setValue(storeId.toString());
        filter.setOperator(EQUAL);
        return filter;
    }

    public static HubSpotSearchFilter buildFilterForTicketByOrder(Long orderId) {

        HubSpotSearchFilter filter = new HubSpotSearchFilter();
        filter.setPropertyName(ORDER_ID);
        filter.setValue(orderId.toString());
        filter.setOperator(EQUAL);
        return filter;
    }
}