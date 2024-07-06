package instrumental.kiwi.hubspot.provider.company;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import instrumental.kiwi.exception.BackendException;
import instrumental.kiwi.hubspot.provider.HubSpotRequestBuilder;
import instrumental.kiwi.hubspot.provider.auth.HubSpotAuthProvider;
import instrumental.kiwi.store.request.StoreRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

import static instrumental.kiwi.exception.MsgCode.OOPS_ERROR;
import static instrumental.kiwi.hubspot.endpoint.HubSpotEndpoint.POST_COMPANY;
import static instrumental.kiwi.hubspot.param.HubSpotProductParam.ID;
import static instrumental.kiwi.security.token.prefix.TokenPrefix.BEARER_PREFIX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class HubSpotCompanyProvider {

    @Value("${HUBSPOT_API_URL}")
    private String HUBSPOT_API_URL;

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final HubSpotAuthProvider hubSpotAuthProvider;
    private final HubSpotRequestBuilder hubSpotRequestBuilder;

    /**
    * A Store represents a HubSpot Company Object in Kiwi's Business Model
    * */
    public String postCompany(StoreRequest storeRequest) {

        Map<String, Object> request = hubSpotRequestBuilder.buildForPostCompany(storeRequest);
        String hubspotAccessToken = hubSpotAuthProvider.resolveHubspotAccessToken();

        return restClient.post()
                .uri(HUBSPOT_API_URL + POST_COMPANY)
                .header(AUTHORIZATION, BEARER_PREFIX + hubspotAccessToken)
                .body(request)
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(), (requestSent, response) -> {
                    throw new BackendException(OOPS_ERROR);
                })
                .toEntity(String.class)
                .getBody();
    }

    public String extractCompanyId(String createdLineItem) {
        try {
            JsonNode root = objectMapper.readTree(createdLineItem);
            return root.path(ID).asText();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}