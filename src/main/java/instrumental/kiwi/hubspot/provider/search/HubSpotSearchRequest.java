package instrumental.kiwi.hubspot.provider.search;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static instrumental.kiwi.exception.FieldValidationError.FIELD_REQUIRED;
import static instrumental.kiwi.hubspot.constant.ObjectType.*;
import static instrumental.kiwi.hubspot.param.HubSpotDealParam.DEAL_NAME;
import static instrumental.kiwi.hubspot.param.HubSpotProductParam.NAME;
import static instrumental.kiwi.hubspot.provider.search.HubSpotSearchFilter.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class HubSpotSearchRequest {

    @NotNull(message = FIELD_REQUIRED)
    private Integer limit;

    @NotNull(message = FIELD_REQUIRED)
    private String objectType;

    @NotNull(message = FIELD_REQUIRED)
    private List<String> properties;

    @NotNull(message = FIELD_REQUIRED)
    private List<HubSpotSearchFilter> filters;

    public static HubSpotSearchRequest buildForProductByStore(Long storeId, Integer limit) {

        HubSpotSearchRequest hubSpotSearchRequest = new HubSpotSearchRequest();

        HubSpotSearchFilter filter = buildFilterForProductsByStore(storeId);
        hubSpotSearchRequest.filters = new ArrayList<>(List.of(filter));
        hubSpotSearchRequest.properties = buildPropertiesForProduct();
        hubSpotSearchRequest.objectType = PRODUCT;
        hubSpotSearchRequest.limit = limit;

        return hubSpotSearchRequest;
    }

    public static HubSpotSearchRequest buildForContactsByStore(Long storeId, Integer limit) {

        HubSpotSearchRequest hubSpotSearchRequest = new HubSpotSearchRequest();

        HubSpotSearchFilter filter = buildFilterForContactsByStore(storeId);
        hubSpotSearchRequest.filters = new ArrayList<>(List.of(filter));
        hubSpotSearchRequest.properties = buildPropertiesForContact();
        hubSpotSearchRequest.objectType = CONTACT;
        hubSpotSearchRequest.limit = limit;
        return hubSpotSearchRequest;
    }

    public static HubSpotSearchRequest buildForDealsByStore(Long storeId, Integer limit) {

        HubSpotSearchRequest hubSpotSearchRequest = new HubSpotSearchRequest();

        HubSpotSearchFilter filter = buildFilterForDealsByStore(storeId);
        hubSpotSearchRequest.filters = new ArrayList<>(List.of(filter));
        hubSpotSearchRequest.properties = buildPropertiesForDeal();
        hubSpotSearchRequest.objectType = DEAL;
        hubSpotSearchRequest.limit = limit;

        return hubSpotSearchRequest;
    }

    public static HubSpotSearchRequest buildForTicketByOrder(Long orderId, Integer limit) {

        HubSpotSearchRequest hubSpotSearchRequest = new HubSpotSearchRequest();

        HubSpotSearchFilter filter = buildFilterForTicketByOrder(orderId);
        hubSpotSearchRequest.filters = new ArrayList<>(List.of(filter));
        hubSpotSearchRequest.properties = buildPropertiesForTicket();
        hubSpotSearchRequest.objectType = TICKET;
        hubSpotSearchRequest.limit = limit;

        return hubSpotSearchRequest;
    }

    private static List<String> buildPropertiesForTicket() {
        return new ArrayList<>(List.of(NAME));
    }

    private static List<String> buildPropertiesForContact() {
        return new ArrayList<>(List.of(NAME));
    }

    private static List<String> buildPropertiesForProduct() {
        return new ArrayList<>(List.of(NAME));
    }

    private static List<String> buildPropertiesForDeal() {
        return new ArrayList<>(List.of(DEAL_NAME));
    }
}