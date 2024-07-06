package instrumental.kiwi.deal.service;

import instrumental.kiwi.hubspot.provider.deal.HubSpotDealProvider;
import instrumental.kiwi.hubspot.provider.deal.HubSpotDealRequest;
import instrumental.kiwi.hubspot.provider.search.HubSpotSearchProvider;
import instrumental.kiwi.hubspot.provider.search.HubSpotSearchRequest;
import instrumental.kiwi.line_item.request.AssociationRequest;
import instrumental.kiwi.line_item.request.AssociationTo;
import instrumental.kiwi.line_item.request.AssociationType;
import instrumental.kiwi.order.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static instrumental.kiwi.hubspot.constant.AssociationCategory.HUBSPOT_DEFINED;
import static instrumental.kiwi.hubspot.constant.AssociationTypeId.DEAL_TO_ORDER;
import static instrumental.kiwi.hubspot.provider.search.HubSpotSearchRequest.buildForDealsByStore;

@Service
@RequiredArgsConstructor
public class DealService {

    @Value("${HUBSPOT_KIWI_OWNER_ID}")
    private String HUBSPOT_KIWI_OWNER_ID;

    private final HubSpotDealProvider hubSpotDealProvider;
    private final HubSpotSearchProvider hubSpotSearchProvider;

    public String getAllDealsByStore(Long storeId, Integer limit) {

        HubSpotSearchRequest searchRequest = buildForDealsByStore(storeId, limit);
        return hubSpotSearchProvider.search(searchRequest);
    }

    public void createDealForOrder(Order order) {

        String amount = order.getTotalPrice().toString();
        String parsedCreatedOrderId = order.getOrderId().toString();
        String storeId =  order.getCustomer().getStore().getStoreId().toString();
        AssociationRequest associationRequest = buildAssociationRequest(parsedCreatedOrderId);
        List<AssociationRequest> associations = new ArrayList<>(List.of(associationRequest));
        HubSpotDealRequest dealRequest = new HubSpotDealRequest(amount, storeId);
        hubSpotDealProvider.postDeal(dealRequest, associations);
    }

    private AssociationRequest buildAssociationRequest(String createdOrderId) {

        AssociationTo associationTo = new AssociationTo(createdOrderId);
        AssociationType associationType = new AssociationType(HUBSPOT_DEFINED, DEAL_TO_ORDER);
        List<AssociationType> associationTypes = new ArrayList<>(List.of(associationType));
        return new AssociationRequest(associationTo, associationTypes);
    }
}