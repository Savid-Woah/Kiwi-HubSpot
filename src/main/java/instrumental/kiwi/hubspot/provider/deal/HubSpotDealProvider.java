package instrumental.kiwi.hubspot.provider.deal;

import instrumental.kiwi.hubspot.provider.HubSpotRequestBuilder;
import instrumental.kiwi.hubspot.provider.auth.HubSpotAuthProvider;
import instrumental.kiwi.line_item.request.AssociationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

import static instrumental.kiwi.hubspot.endpoint.HubSpotEndpoint.POST_DEAL;
import static instrumental.kiwi.security.token.prefix.TokenPrefix.BEARER_PREFIX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class HubSpotDealProvider {

    @Value("${HUBSPOT_API_URL}")
    private String HUBSPOT_API_URL;

    private final RestClient restClient;
    private final HubSpotAuthProvider hubSpotAuthProvider;
    private final HubSpotRequestBuilder hubSpotRequestBuilder;

    public void postDeal(HubSpotDealRequest dealRequest, List<AssociationRequest> associations) {

        Map<String, Object> request = hubSpotRequestBuilder.buildForPostDeal(dealRequest, associations);
        String hubspotAccessToken = hubSpotAuthProvider.resolveHubspotAccessToken();

        restClient.post()
                .uri(HUBSPOT_API_URL + POST_DEAL)
                .header(AUTHORIZATION, BEARER_PREFIX + hubspotAccessToken)
                .body(request)
                .retrieve()
                .toEntity(String.class)
                .getBody();
    }
}