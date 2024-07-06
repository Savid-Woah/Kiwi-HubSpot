package instrumental.kiwi.security.config.service;

import instrumental.kiwi.security.token.model.Token;
import instrumental.kiwi.security.token.repository.TokenRepository;
import instrumental.kiwi.security.user.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Transactional
@RequiredArgsConstructor
public class JwtService {

    @Value("${JWT_SECRET}")
    private String secretKey;

    private final TokenRepository tokenRepository;

    private static final String ROLE = "role";
    private static final String USER_ID = "user_id";
    private static final String ACCESS_TOKEN = "Access_Token";

    private Key getSigningKey() {

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token) {

        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {

        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {

        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get(USER_ID)
                .toString();
    }

    private Date extractExpirationDate(String token) {

        return extractClaim(token, Claims::getExpiration);
    }

    public String generateToken(Map<String, Object> extraClaims, String subject) {

        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(User user) {

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put(USER_ID, user.getUserId().toString());
        extraClaims.put(ROLE, user.getRole());
        return generateToken(extraClaims, ACCESS_TOKEN);
    }

    private boolean isTokenExpired(String token) {

        return extractExpirationDate(token).before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails){

        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public void saveUserToken(User user, String jwt) {

        Token token = Token.buildForSystem(jwt, user);
        tokenRepository.save(token);
    }
}