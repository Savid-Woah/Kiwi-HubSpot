package instrumental.kiwi.hubspot.provider.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import instrumental.kiwi.hubspot.provider.HubSpotRequestBuilder;
import instrumental.kiwi.hubspot.provider.auth.HubSpotAuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

import static instrumental.kiwi.hubspot.endpoint.HubSpotEndpoint.*;
import static instrumental.kiwi.security.token.prefix.TokenPrefix.BEARER_PREFIX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class HubSpotSearchProvider {

    @Value("${HUBSPOT_API_URL}")
    private String HUBSPOT_API_URL;

    private final RestClient restClient;
    private final HubSpotAuthProvider hubSpotAuthProvider;
    private final HubSpotRequestBuilder hubSpotRequestBuilder;

    public String search(HubSpotSearchRequest searchRequest) {

        String objectType = searchRequest.getObjectType();
        Map<String, Object> request = hubSpotRequestBuilder.buildForSearch(searchRequest);
        String hubspotAccessToken = hubSpotAuthProvider.resolveHubspotAccessToken();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            System.out.println("REQUEST: " + objectMapper.writeValueAsString(request));
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }

        return restClient.post()
                .uri(HUBSPOT_API_URL + SEARCH_PREFIX + objectType + SEARCH_SUFFIX)
                .header(AUTHORIZATION, BEARER_PREFIX + hubspotAccessToken)
                .body(request)
                .retrieve()
                .toEntity(String.class)
                .getBody();
    }
}