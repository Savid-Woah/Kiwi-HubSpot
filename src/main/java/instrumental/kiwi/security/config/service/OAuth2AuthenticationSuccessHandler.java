package instrumental.kiwi.security.config.service;

import instrumental.kiwi.security.auth.service.CookieService;
import instrumental.kiwi.security.auth.service.OAuth2Service;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Value("${FRONT_END_URL}")
    private String FRONT_END_URL;
    private final OAuth2Service oAuth2Service;
    private final CookieService cookieService;

    @Override
    public void onAuthenticationSuccess(

            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication

    ) throws IOException {

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        DefaultOAuth2User oauth2User = (DefaultOAuth2User) oauthToken.getPrincipal();
        String jwt = oAuth2Service.getJwtFromOAuth2Flow(oauth2User);
        cookieService.addCookie(response, "jwt", jwt, -1);
        response.sendRedirect(FRONT_END_URL+"/home");
    }
}