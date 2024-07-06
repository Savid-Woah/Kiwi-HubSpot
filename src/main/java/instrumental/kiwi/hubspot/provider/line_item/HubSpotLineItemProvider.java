package instrumental.kiwi.hubspot.provider.line_item;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import instrumental.kiwi.hubspot.provider.HubSpotRequestBuilder;
import instrumental.kiwi.hubspot.provider.auth.HubSpotAuthProvider;
import instrumental.kiwi.line_item.request.LineItemRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

import static instrumental.kiwi.hubspot.endpoint.HubSpotEndpoint.POST_LINE_ITEM;
import static instrumental.kiwi.hubspot.param.HubSpotProductParam.ID;
import static instrumental.kiwi.security.token.prefix.TokenPrefix.BEARER_PREFIX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class HubSpotLineItemProvider {

    @Value("${HUBSPOT_API_URL}")
    private String HUBSPOT_API_URL;

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final HubSpotAuthProvider hubSpotAuthProvider;
    private final HubSpotRequestBuilder hubSpotRequestBuilder;

    public String postLineItem(LineItemRequest lineItemRequest) {

        Map<String, Object> request = hubSpotRequestBuilder.buildForLineItem(lineItemRequest);
        String hubspotAccessToken = hubSpotAuthProvider.resolveHubspotAccessToken();

        return restClient.post()
                .uri(HUBSPOT_API_URL + POST_LINE_ITEM)
                .header(AUTHORIZATION, BEARER_PREFIX + hubspotAccessToken)
                .body(request)
                .retrieve()
                .toEntity(String.class)
                .getBody();
    }

    public String extractLineItemId(String createdLineItem) {
        try {
            JsonNode root = objectMapper.readTree(createdLineItem);
            return root.path(ID).asText();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}