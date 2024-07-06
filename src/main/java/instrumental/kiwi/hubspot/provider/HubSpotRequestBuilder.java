package instrumental.kiwi.hubspot.provider;

import instrumental.kiwi.contact.request.ContactRequest;
import instrumental.kiwi.hubspot.provider.deal.HubSpotDealRequest;
import instrumental.kiwi.hubspot.provider.search.HubSpotSearchRequest;
import instrumental.kiwi.line_item.request.AssociationRequest;
import instrumental.kiwi.line_item.request.LineItemRequest;
import instrumental.kiwi.order.request.OrderRequest;
import instrumental.kiwi.product.request.ProductRequest;
import instrumental.kiwi.store.request.StoreRequest;
import instrumental.kiwi.ticket.request.TicketRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static instrumental.kiwi.hubspot.param.HubSpotAuthParam.*;
import static instrumental.kiwi.hubspot.param.HubSpotCompanyParam.*;
import static instrumental.kiwi.hubspot.param.HubSpotCompanyParam.LIFECYCLESTAGE;
import static instrumental.kiwi.hubspot.param.HubSpotCompanyParam.NAME;
import static instrumental.kiwi.hubspot.param.HubSpotCompanyParam.PHONE;
import static instrumental.kiwi.hubspot.param.HubSpotContactParam.*;
import static instrumental.kiwi.hubspot.param.HubSpotDealParam.*;
import static instrumental.kiwi.hubspot.param.HubSpotLineItemParam.HS_PRODUCT_ID;
import static instrumental.kiwi.hubspot.param.HubSpotLineItemParam.QUANTITY;
import static instrumental.kiwi.hubspot.param.HubSpotOrderParam.*;
import static instrumental.kiwi.hubspot.param.HubSpotOrderParam.PROPERTIES;
import static instrumental.kiwi.hubspot.param.HubSpotProductParam.*;
import static instrumental.kiwi.hubspot.param.HubSpotProductParam.COMPANY_ID;
import static instrumental.kiwi.hubspot.param.HubSpotSearchParam.*;
import static instrumental.kiwi.hubspot.param.HubSpotTicketParam.*;

@Component
@RequiredArgsConstructor
public class HubSpotRequestBuilder {

    @Value("${HUBSPOT_CLIENT_ID}")
    private String HUBSPOT_CLIENT_ID;
    @Value("${HUBSPOT_REDIRECT_URI}")
    private String HUBSPOT_REDIRECT_URI;
    @Value("${HUBSPOT_CLIENT_SECRET}")
    private String HUBSPOT_CLIENT_SECRET;

    public MultiValueMap<String, String> buildForAccessToken(String code) {

        MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        request.add(CLIENT_SECRET, HUBSPOT_CLIENT_SECRET);
        request.add(REDIRECT_URI, HUBSPOT_REDIRECT_URI);
        request.add(GRANT_TYPE, AUTHORIZATION_CODE);
        request.add(CLIENT_ID, HUBSPOT_CLIENT_ID);
        request.add(CODE, code);
        return request;
    }

    public MultiValueMap<String, String> buildForAccessTokenFromRefreshToken(String refreshToken) {

        MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        request.add(CLIENT_SECRET, HUBSPOT_CLIENT_SECRET);
        request.add(REDIRECT_URI, HUBSPOT_REDIRECT_URI);
        request.add(CLIENT_ID, HUBSPOT_CLIENT_ID);
        request.add(REFRESH_TOKEN, refreshToken);
        request.add(GRANT_TYPE, REFRESH_TOKEN);
        return request;
    }

    public Map<String, Object> buildForPostCompany(StoreRequest storeRequest) {

        Map<String, Object> properties = buildCompanyProperties(storeRequest);
        return new HashMap<>(Map.of(PROPERTIES, properties));
    }

    public Map<String, Object> buildForPostContact(ContactRequest contactRequest) {

        Map<String, Object> properties = buildContactProperties(contactRequest);
        return new HashMap<>(Map.of(PROPERTIES, properties));
    }

    public Map<String, Object> buildForPostProduct(ProductRequest productRequest) {

        Map<String, Object> properties = buildProductProperties(productRequest);
        return new HashMap<>(Map.of(PROPERTIES, properties));
    }

    public Map<String, Object> buildForLineItem(LineItemRequest lineItemRequest) {

        Map<String, Object> properties = buildLineItemProperties(lineItemRequest);
        return new HashMap<>(Map.of(PROPERTIES, properties));
    }

    public Map<String, Object> buildForPostOrder(OrderRequest orderRequest, List<AssociationRequest> associations) {

        Map<String, Object> properties = buildOrderProperties(orderRequest);
        return new HashMap<>(Map.of(ASSOCIATIONS, associations, PROPERTIES, properties));
    }

    public Map<String, Object> buildForPostDeal(HubSpotDealRequest dealRequest, List<AssociationRequest> associations) {

        Map<String, Object> properties = buildDealProperties(dealRequest);
        return new HashMap<>(Map.of(ASSOCIATIONS, associations, PROPERTIES, properties));
    }

    public Map<String, Object> buildForPostTicket(TicketRequest ticketRequest, List<AssociationRequest> associations) {

        Map<String, Object> properties = buildTicketProperties(ticketRequest);
        return new HashMap<>(Map.of(ASSOCIATIONS, associations, PROPERTIES, properties));
    }

    public Map<String, Object> buildForSearch(HubSpotSearchRequest searchRequest) {

        Map<String, Object> request = new HashMap<>();
        request.put(AFTER, 0);
        request.put(LIMIT, searchRequest.getLimit());
        request.put(FILTERS, searchRequest.getFilters());
        request.put(PROPERTIES, searchRequest.getProperties());
        return request;
    }

    private Map<String, Object> buildCompanyProperties(StoreRequest storeRequest) {

        Map<String, Object> companyProperties = new HashMap<>();
        companyProperties.put(LIFECYCLESTAGE, storeRequest.getLifeCycleStage());
        companyProperties.put(INDUSTRY, storeRequest.getIndustry());
        companyProperties.put(PHONE, storeRequest.getPhoneNumber());
        companyProperties.put(DOMAIN, storeRequest.getDomain());
        companyProperties.put(STATE, storeRequest.getState());
        companyProperties.put(NAME, storeRequest.getName());
        companyProperties.put(CITY, storeRequest.getCity());
        return companyProperties;
    }

    private Map<String, Object> buildContactProperties(ContactRequest contactRequest) {

        Map<String, Object> contactProperties = new HashMap<>();
        contactProperties.put(LIFECYCLESTAGE, contactRequest.getLifecyclestage());
        contactProperties.put(FIRSTNAME, contactRequest.getFirstname());
        contactProperties.put(COMPANY_ID, contactRequest.getStoreId());
        contactProperties.put(LASTNAME, contactRequest.getLastname());
        contactProperties.put(COMPANY, contactRequest.getCompany());
        contactProperties.put(WEBSITE, contactRequest.getWebsite());
        contactProperties.put(EMAIL, contactRequest.getEmail());
        contactProperties.put(PHONE, contactRequest.getPhone());
        return contactProperties;
    }

    public Map<String, Object> buildProductProperties(ProductRequest productRequest) {

        Map<String, Object> productProperties = new HashMap<>();
        productProperties.put(HS_RECURRING_BILLING_PERIOD, productRequest.getHsRecurringBillingPeriod());
        productProperties.put(HS_COST_OF_GOODS_SOLD, productRequest.getHsCostOfGoodsSold());
        productProperties.put(DESCRIPTION, productRequest.getDescription());
        productProperties.put(COMPANY_ID, productRequest.getStoreId());
        productProperties.put(HS_SKU, productRequest.getHsSku());
        productProperties.put(PRICE, productRequest.getPrice());
        productProperties.put(NAME, productRequest.getName());
        return  productProperties;
    }

    public Map<String, Object> buildLineItemProperties(LineItemRequest lineItemRequest) {

        Map<String, Object> lineItemProperties = new HashMap<>();
        lineItemProperties.put(HS_PRODUCT_ID, lineItemRequest.getHsProductId());
        lineItemProperties.put(QUANTITY, lineItemRequest.getQuantity());
        lineItemProperties.put(NAME, lineItemRequest.getName());
        return  lineItemProperties;
    }

    private Map<String, Object> buildOrderProperties(OrderRequest orderRequest) {

        Map<String, Object> orderProperties = new HashMap<>();
        orderProperties.put(HS_SHIPPING_ADDRESS_STREET, orderRequest.getHsShippingAddressStreet());
        orderProperties.put(HS_SHIPPING_ADDRESS_STATE, orderRequest.getHsShippingAddressState());
        orderProperties.put(HS_SHIPPING_ADDRESS_CITY, orderRequest.getHsShippingAddressCity());
        orderProperties.put(HS_FULFILLMENT_STATUS, orderRequest.getHsFulfillmentStatus());
        orderProperties.put(HS_CURRENCY_CODE, orderRequest.getHsCurrencyCode());
        orderProperties.put(HS_SOURCE_STORE, orderRequest.getHsSourceStore());
        orderProperties.put(HS_ORDER_NAME, orderRequest.getHsOrderName());
        return orderProperties;
    }

    public Map<String, Object> buildDealProperties(HubSpotDealRequest dealRequest) {

        Map<String, Object> dealProperties = new HashMap<>();
        dealProperties.put(COMPANY_ID, dealRequest.getCompanyId());
        dealProperties.put(CLOSE_DATE, dealRequest.getClosedate() + "Z");
        dealProperties.put(DEAL_NAME, dealRequest.getDealname());
        dealProperties.put(PIPE_LINE, dealRequest.getPipeline());
        dealProperties.put(AMOUNT, dealRequest.getAmount());
        return  dealProperties;
    }

    public Map<String, Object> buildTicketProperties(TicketRequest ticketRequest) {

        Map<String, Object> ticketProperties = new HashMap<>();
        ticketProperties.put(HS_TICKET_PRIORITY, ticketRequest.getHsTicketPriority());
        ticketProperties.put(HS_PIPELINE_STAGE, ticketRequest.getHsPipelineStage());
        ticketProperties.put(HS_PIPELINE, ticketRequest.getHsPipeline());
        ticketProperties.put(ORDER_ID, ticketRequest.getOrderId());
        ticketProperties.put(SUBJECT, ticketRequest.getSubject());
        return  ticketProperties;
    }
}