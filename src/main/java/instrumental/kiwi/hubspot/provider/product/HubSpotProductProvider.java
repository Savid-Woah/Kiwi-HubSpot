package instrumental.kiwi.hubspot.provider.product;

import instrumental.kiwi.exception.BackendException;
import instrumental.kiwi.hubspot.provider.HubSpotRequestBuilder;
import instrumental.kiwi.hubspot.provider.auth.HubSpotAuthProvider;
import instrumental.kiwi.product.request.ProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.UUID;

import static instrumental.kiwi.exception.MsgCode.OOPS_ERROR;
import static instrumental.kiwi.hubspot.endpoint.HubSpotEndpoint.GET_PRODUCTS_BY_COMPANY;
import static instrumental.kiwi.hubspot.endpoint.HubSpotEndpoint.POST_PRODUCT;
import static instrumental.kiwi.security.token.prefix.TokenPrefix.BEARER_PREFIX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class HubSpotProductProvider {

    @Value("${HUBSPOT_API_URL}")
    private String HUBSPOT_API_URL;

    private final RestClient restClient;
    private final HubSpotAuthProvider hubSpotAuthProvider;
    private final HubSpotRequestBuilder hubSpotRequestBuilder;

    public String postProduct(ProductRequest productRequest) {

        Map<String, Object> request = hubSpotRequestBuilder.buildForPostProduct(productRequest);
        String hubspotAccessToken = hubSpotAuthProvider.resolveHubspotAccessToken();

        return restClient.post()
                .uri(HUBSPOT_API_URL + POST_PRODUCT)
                .header(AUTHORIZATION, BEARER_PREFIX + hubspotAccessToken)
                .body(request)
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(), (requestSent, response) -> {
                    throw new BackendException(OOPS_ERROR);
                })
                .toEntity(String.class)
                .getBody();
    }

    public String getProductsByCompany(UUID companyId) {

        String hubspotAccessToken = hubSpotAuthProvider.resolveHubspotAccessToken();

        return restClient.get()
                .uri(HUBSPOT_API_URL + GET_PRODUCTS_BY_COMPANY)
                .header(AUTHORIZATION, BEARER_PREFIX + hubspotAccessToken)
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(), (requestSent, response) -> {
                    throw new BackendException(OOPS_ERROR);
                })
                .toEntity(String.class)
                .getBody();
    }
}