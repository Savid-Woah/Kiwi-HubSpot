package instrumental.kiwi.hubspot.provider.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import instrumental.kiwi.exception.BackendException;
import instrumental.kiwi.hubspot.provider.HubSpotRequestBuilder;
import instrumental.kiwi.security.token.model.Token;
import instrumental.kiwi.security.token.repository.TokenRepository;
import instrumental.kiwi.security.user.model.User;
import instrumental.kiwi.security.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;

import static instrumental.kiwi.exception.MsgCode.*;
import static instrumental.kiwi.hubspot.endpoint.HubSpotEndpoint.GET_ACCESS_TOKEN;
import static instrumental.kiwi.hubspot.param.HubSpotAuthParam.*;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

@Component
@RequiredArgsConstructor
public class HubSpotAuthProvider {

    @Value("${ADMIN_EMAIL}")
    private String ADMIN_EMAIL;
    @Value("${HUBSPOT_API_URL}")
    private String HUBSPOT_API_URL;

    private final RestClient restClient;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final HubSpotRequestBuilder hubSpotRequestBuilder;

    public String generateOAuthAccessToken(String code) {

        MultiValueMap<String, String> request = hubSpotRequestBuilder
                .buildForAccessToken(code);

        String response = restClient.post()
                .uri(HUBSPOT_API_URL + GET_ACCESS_TOKEN)
                .contentType(APPLICATION_FORM_URLENCODED)
                .body(request)
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(), (requestSent, responseGotten) -> {
                    System.out.println(responseGotten.getBody());
                    throw new BackendException(OOPS_ERROR);
                })
                .toEntity(String.class)
                .getBody();

        try{

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode responseJSON = objectMapper.readTree(response);
            return saveHubspotToken(responseJSON);

        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public String generateOAuthAccessTokenFromRefreshToken(String refreshToken) {

        MultiValueMap<String, String> request = hubSpotRequestBuilder
                .buildForAccessTokenFromRefreshToken(refreshToken);

        String response = restClient.post()
                .uri(HUBSPOT_API_URL + GET_ACCESS_TOKEN)
                .contentType(APPLICATION_FORM_URLENCODED)
                .body(request)
                .retrieve()
                .toEntity(String.class)
                .getBody();

        try{

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode responseJSON = objectMapper.readTree(response);
            return updateHubspotToken(responseJSON);

        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public String resolveHubspotAccessToken() {

        User user = userRepository.findByEmail(ADMIN_EMAIL).orElseThrow(()
                -> new BackendException(USER_NOT_FOUND));

        Token hubspotAccessToken = tokenRepository.findHubspotAccessTokenByUser(user.getUserId())
                .orElseThrow(() -> new BackendException(NO_HUBSPOT_TOKEN_PRESENT));

        if(isValidAccessToken(hubspotAccessToken)) return hubspotAccessToken.getToken();
        else return generateOAuthAccessTokenFromRefreshToken(hubspotAccessToken.getRefreshToken());
    }

    private boolean isValidAccessToken(Token hubspotAccessToken) {
        return hubspotAccessToken.getExpirationTime().isAfter(LocalDateTime.now());
    }

    private String saveHubspotToken(JsonNode responseJson) {

        User user = userRepository.findByEmail(ADMIN_EMAIL).orElseThrow(()
                -> new BackendException(USER_NOT_FOUND));

        String accessToken = responseJson.get(ACCESS_TOKEN).asText();
        String refreshToken = responseJson.get(REFRESH_TOKEN).asText();
        int expiresIn = responseJson.get(EXPIRES_IN).asInt();

        LocalDateTime tokenExpiration = LocalDateTime.now().plusSeconds(expiresIn);

        Token hubspotToken = Token.buildForHubspot(accessToken, tokenExpiration, refreshToken, user);
        tokenRepository.save(hubspotToken);

        return accessToken;
    }

    private String updateHubspotToken(JsonNode responseJson) {

        String accessToken = responseJson.get(ACCESS_TOKEN).asText();
        String refreshToken = responseJson.get(REFRESH_TOKEN).asText();

        Token hubspotToken = tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new BackendException(NO_HUBSPOT_TOKEN_PRESENT));

        hubspotToken.setToken(accessToken);
        tokenRepository.save(hubspotToken);
        return accessToken;
    }
}