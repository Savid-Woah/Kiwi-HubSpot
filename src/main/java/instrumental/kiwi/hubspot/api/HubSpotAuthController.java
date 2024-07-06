package instrumental.kiwi.hubspot.api;

import instrumental.kiwi.hubspot.provider.auth.HubSpotAuthProvider;
import instrumental.kiwi.response.annotation.ApiResponder;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpHeaders.LOCATION;

@RestController
@Tag(name = "HubSpot")
@RequiredArgsConstructor
@RequestMapping("kiwi/api/v1/hubspot-oauth/")
public class HubSpotAuthController {

    @Value("${HUBSPOT_OAUTH_URL}")
    public String hubspotOauthRedirectUrl;

    private final HubSpotAuthProvider hubSpotAuthProvider;

    @GetMapping
    public void sendToHubSpotOAuth(HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader(LOCATION, hubspotOauthRedirectUrl);
        httpServletResponse.setStatus(302);
    }

    @GetMapping(path = "call-back")
    public String HubSpotOAuthCallback(@RequestParam("code") String code) {
        return "Your HubSpot OAuth Code is: " + code;
    }

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