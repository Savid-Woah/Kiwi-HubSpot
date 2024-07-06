package instrumental.kiwi.hubspot.provider.ticket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import instrumental.kiwi.hubspot.provider.HubSpotRequestBuilder;
import instrumental.kiwi.hubspot.provider.auth.HubSpotAuthProvider;
import instrumental.kiwi.line_item.request.AssociationRequest;
import instrumental.kiwi.ticket.request.TicketRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

import static instrumental.kiwi.hubspot.endpoint.HubSpotEndpoint.POST_TICKET;
import static instrumental.kiwi.security.token.prefix.TokenPrefix.BEARER_PREFIX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class HubSpotTicketProvider {

    @Value("${HUBSPOT_API_URL}")
    private String HUBSPOT_API_URL;

    private final RestClient restClient;
    private final HubSpotAuthProvider hubSpotAuthProvider;
    private final HubSpotRequestBuilder hubSpotRequestBuilder;

    public String postTicket(TicketRequest ticketRequest, List<AssociationRequest> associations) {

        Map<String, Object> request = hubSpotRequestBuilder.buildForPostTicket(ticketRequest, associations);
        String hubspotAccessToken = hubSpotAuthProvider.resolveHubspotAccessToken();

        try {

            ObjectMapper objectMapper = new ObjectMapper();
            String s = objectMapper.writeValueAsString(request);
            System.out.println("REQ: " + s);

        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex.getMessage());
        }

        return restClient.post()
                .uri(HUBSPOT_API_URL + POST_TICKET)
                .header(AUTHORIZATION, BEARER_PREFIX + hubspotAccessToken)
                .body(request)
                .retrieve()
                .toEntity(String.class)
                .getBody();
    }
}