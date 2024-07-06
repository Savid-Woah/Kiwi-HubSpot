package instrumental.kiwi.product.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import instrumental.kiwi.product.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

import static instrumental.kiwi.hubspot.param.HubSpotOrderParam.*;
import static instrumental.kiwi.hubspot.param.HubSpotProductParam.*;
import static instrumental.kiwi.hubspot.param.HubSpotProductParam.ID;

@Component
@RequiredArgsConstructor
public class ProductHubSpotMapper implements Function<String, Product> {

    private final ObjectMapper objectMapper;

    @Override
    public Product apply(String createdHubspotProductJsonString) {

        JsonNode rootNode;

        try {

            rootNode = objectMapper.readTree(createdHubspotProductJsonString);
            JsonNode propertiesNode = rootNode.path(PROPERTIES);

            Product product = new Product();
            product.setProductId(rootNode.path(ID).asLong());
            product.setName(propertiesNode.path(NAME).asText());
            product.setPrice(propertiesNode.path(PRICE).asDouble());
            product.setHsSkU(propertiesNode.path(HS_SKU).asText());
            product.setDescription(propertiesNode.path(DESCRIPTION).asText());
            product.setHsCostOfGoodsSold(propertiesNode.path(HS_COST_OF_GOODS_SOLD).asText());
            product.setHsRecurringBillingPeriod(propertiesNode.path(HS_RECURRING_BILLING_PERIOD).asText());
            return product;

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}