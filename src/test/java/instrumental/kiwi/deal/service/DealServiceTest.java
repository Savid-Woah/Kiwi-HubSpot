package instrumental.kiwi.deal.service;

import instrumental.kiwi.customer.model.Customer;
import instrumental.kiwi.hubspot.provider.deal.HubSpotDealProvider;
import instrumental.kiwi.hubspot.provider.deal.HubSpotDealRequest;
import instrumental.kiwi.hubspot.provider.search.HubSpotSearchProvider;
import instrumental.kiwi.line_item.request.AssociationRequest;
import instrumental.kiwi.line_item.request.AssociationTo;
import instrumental.kiwi.line_item.request.AssociationType;
import instrumental.kiwi.order.model.Order;
import instrumental.kiwi.store.model.Store;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static instrumental.kiwi.hubspot.constant.AssociationCategory.HUBSPOT_DEFINED;
import static instrumental.kiwi.hubspot.constant.AssociationTypeId.DEAL_TO_ORDER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DealServiceTest {

    @Mock
    private HubSpotDealProvider hubSpotDealProvider;
    @Mock
    private HubSpotSearchProvider hubSpotSearchProvider;
    @InjectMocks
    private DealService dealService;

    @Test
    @DisplayName(value = "Should create deal for order")
    void shouldCreateDealForOrder() {

        // Given "an existing order"
        Order order = new Order();
        order.setTotalPrice(100.00);
        order.setOrderId(123L);

        Customer customer = new Customer();
        Store store = new Store();
        store.setStoreId(1L);
        customer.setStore(store);
        order.setCustomer(customer);

        // and "an association request built for the order"
        String parsedCreatedOrderId = order.getOrderId().toString();
        AssociationRequest associationRequest = buildAssociationRequest(parsedCreatedOrderId);
        List<AssociationRequest> associations = new ArrayList<>(List.of(associationRequest));

        // and "a deal request built for the order"
        String amount = order.getTotalPrice().toString();
        String storeId = order.getCustomer().getStore().getStoreId().toString();
        HubSpotDealRequest dealRequest = new HubSpotDealRequest(amount, storeId);

        // and "mocked behaviour"
        doNothing().when(hubSpotDealProvider).postDeal(any(HubSpotDealRequest.class), anyList());

        // When
        dealService.createDealForOrder(order);

        // Then
        ArgumentCaptor<HubSpotDealRequest> dealRequestCaptor = forClass(HubSpotDealRequest.class);
        ArgumentCaptor<List<AssociationRequest>> associationsCaptor = forClass(List.class);

        verify(hubSpotDealProvider, times(1)).postDeal(
                dealRequestCaptor.capture(), associationsCaptor.capture());
        HubSpotDealRequest capturedDealRequest = dealRequestCaptor.getValue();
        List<AssociationRequest> capturedAssociations = associationsCaptor.getValue();
        assertEquals(dealRequest.getAmount(), capturedDealRequest.getAmount());
        assertEquals(dealRequest.getCompanyId(), capturedDealRequest.getCompanyId());
        assertEquals(associations, capturedAssociations);
    }

    private AssociationRequest buildAssociationRequest(String createdOrderId) {

        AssociationTo associationTo = new AssociationTo(createdOrderId);
        AssociationType associationType = new AssociationType(HUBSPOT_DEFINED, DEAL_TO_ORDER);
        List<AssociationType> associationTypes = new ArrayList<>(List.of(associationType));
        return new AssociationRequest(associationTo, associationTypes);
    }
}
