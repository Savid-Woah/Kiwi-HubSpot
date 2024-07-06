package instrumental.kiwi.hubspot.api;

import instrumental.kiwi.hubspot.provider.auth.HubSpotAuthProvider;
import instrumental.kiwi.response.annotation.ApiResponder;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "HubSpot")
@RequiredArgsConstructor
@RequestMapping("kiwi/api/v1/oauth/")
public class HubSpotAuthController {

    private final HubSpotAuthProvider hubSpotAuthProvider;

    @ApiResponder
    @PostMapping(path = "get-access-token")
    public String generateOAuthAccessToken(@RequestParam("code") String code) {
        return hubSpotAuthProvider.generateOAuthAccessToken(code);
    }

    @Hidden
    @PostMapping(path = "get-refresh-token")
    public String generateOAuthAccessTokenFromRefreshToken(@RequestParam("refresh") String refresh) {
        return hubSpotAuthProvider.generateOAuthAccessTokenFromRefreshToken(refresh);
    }

    @Hidden
    @PostMapping(path = "resolve-access-token")
    public String resolveHubspotAccessToken() {
        return hubSpotAuthProvider.resolveHubspotAccessToken();
    }
}