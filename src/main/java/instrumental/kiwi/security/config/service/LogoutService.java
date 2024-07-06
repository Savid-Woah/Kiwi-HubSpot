package instrumental.kiwi.security.config.service;

import instrumental.kiwi.security.auth.service.CookieService;
import instrumental.kiwi.security.token.model.Token;
import instrumental.kiwi.security.token.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import static instrumental.kiwi.security.token.prefix.TokenPrefix.BEARER_PREFIX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final CookieService cookieService;
    private final TokenRepository tokenRepository;

    @Override
    public void logout(

            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            Authentication authentication
    ) {

        final String authHeader = request.getHeader(AUTHORIZATION);

        if(authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            return;
        }

        final String jwt = authHeader.substring(7);

        Token storedToken = tokenRepository.findByToken(jwt)
                .orElse(null);

        if(storedToken != null) {

            storedToken.setRevoked(true);
            storedToken.setExpired(true);
            tokenRepository.save(storedToken);
        }

        cookieService.expireCookie(response, "jwt");
    }
}