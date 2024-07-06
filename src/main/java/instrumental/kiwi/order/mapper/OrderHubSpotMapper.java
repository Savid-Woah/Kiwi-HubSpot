package instrumental.kiwi.order.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import instrumental.kiwi.order.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

import static instrumental.kiwi.hubspot.param.HubSpotOrderParam.*;

@Component
@RequiredArgsConstructor
public class OrderHubSpotMapper implements Function<String, Order> {

    private final ObjectMapper objectMapper;

    @Override
    public Order apply(String createdOrderJsonString) {

        JsonNode rootNode;

        try {

            rootNode = objectMapper.readTree(createdOrderJsonString);
            JsonNode propertiesNode = rootNode.path(PROPERTIES);

            Order order = new Order();
            order.setOrderId(rootNode.path(ID).asLong());
            order.setHsOrderName(propertiesNode.path(HS_ORDER_NAME).asText());
            order.setHsCurrencyCode(propertiesNode.path(HS_CURRENCY_CODE).asText());
            order.setHsSourceStore(propertiesNode.path(HS_SOURCE_STORE).asText());
            order.setHsFulfillmentStatus(propertiesNode.path(HS_FULFILLMENT_STATUS).asText());
            order.setHsShippingAddressCity(propertiesNode.path(HS_SHIPPING_ADDRESS_CITY).asText());
            order.setHsShippingAddressState(propertiesNode.path(HS_SHIPPING_ADDRESS_STATE).asText());
            order.setHsShippingAddressStreet(propertiesNode.path(HS_SHIPPING_ADDRESS_STREET).asText());
            return order;

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}