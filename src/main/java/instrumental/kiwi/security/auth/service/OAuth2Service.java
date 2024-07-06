package instrumental.kiwi.security.auth.service;

import instrumental.kiwi.security.config.service.JwtService;
import instrumental.kiwi.security.user.model.User;
import instrumental.kiwi.security.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static instrumental.kiwi.security.user.enums.Role.STORE;

@Service
@RequiredArgsConstructor
public class OAuth2Service {

    private static final String EMAIL = "email";
    private final UserService userService;
    private final JwtService jwtService;

    public String getJwtFromOAuth2Flow(DefaultOAuth2User oauth2User) {

        String email = (String) oauth2User.getAttributes().get(EMAIL);
        Optional<User> userOptional = userService.getUserByEmail(email);
        User user = userOptional.orElseGet(() -> userService.createOAuth2User(email, STORE));
        String jwt = jwtService.generateToken(user);
        jwtService.saveUserToken(user, jwt);
        return jwt;
    }
}
