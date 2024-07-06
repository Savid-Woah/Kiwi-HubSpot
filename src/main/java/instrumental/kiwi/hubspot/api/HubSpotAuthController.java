package instrumental.kiwi.hubspot.api;

import instrumental.kiwi.hubspot.provider.auth.HubSpotAuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("kiwi/api/v1/oauth/")
public class HubSpotAuthController {

    private final HubSpotAuthProvider hubSpotAuthProvider;

    @PostMapping(path = "get-access-token")
    public String generateOAuthAccessToken(@RequestParam("code") String code) {
        return hubSpotAuthProvider.generateOAuthAccessToken(code);
    }

    @PostMapping(path = "get-refresh-token")
    public String generateOAuthAccessTokenFromRefreshToken(@RequestParam("refresh") String refresh) {
        return hubSpotAuthProvider.generateOAuthAccessTokenFromRefreshToken(refresh);
    }

    @PostMapping(path = "resolve-access-token")
    public String resolveHubspotAccessToken() {
        return hubSpotAuthProvider.resolveHubspotAccessToken();
    }
}