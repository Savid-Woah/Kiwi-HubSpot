package instrumental.kiwi.hubspot.provider.contact;

import instrumental.kiwi.contact.request.ContactRequest;
import instrumental.kiwi.hubspot.provider.HubSpotRequestBuilder;
import instrumental.kiwi.hubspot.provider.auth.HubSpotAuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.UUID;

import static instrumental.kiwi.hubspot.endpoint.HubSpotEndpoint.*;
import static instrumental.kiwi.security.token.prefix.TokenPrefix.BEARER_PREFIX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class HubSpotContactProvider {

    @Value("${HUBSPOT_API_URL}")
    private String HUBSPOT_API_URL;

    private final RestClient restClient;
    private final HubSpotAuthProvider hubSpotAuthProvider;
    private final HubSpotRequestBuilder hubSpotRequestBuilder;

    /**
     * A HubSpot Contact Object represents a Supplier of a Store
     *
     *
     */
    public String postContact(ContactRequest contactRequest) {

        Map<String, Object> request = hubSpotRequestBuilder.buildForPostContact(contactRequest);
        String hubspotAccessToken = hubSpotAuthProvider.resolveHubspotAccessToken();

        return restClient.post()
                .uri(HUBSPOT_API_URL + POST_CONTACT)
                .header(AUTHORIZATION, BEARER_PREFIX + hubspotAccessToken)
                .body(request)
                .retrieve()
                .toEntity(String.class)
                .getBody();
    }

    public String getContactsByCompany(UUID companyId) {

        String hubspotAccessToken = hubSpotAuthProvider.resolveHubspotAccessToken();

        return restClient.get()
                .uri(HUBSPOT_API_URL + GET_CONTACTS_BY_COMPANY)
                .header(AUTHORIZATION, BEARER_PREFIX + hubspotAccessToken)
                .retrieve()
                .toEntity(String.class)
                .getBody();
    }
}
