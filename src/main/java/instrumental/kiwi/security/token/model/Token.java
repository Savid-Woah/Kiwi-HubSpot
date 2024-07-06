package instrumental.kiwi.security.token.model;

import instrumental.kiwi.security.token.enums.TokenSource;
import instrumental.kiwi.security.token.enums.TokenType;
import instrumental.kiwi.security.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import static instrumental.kiwi.security.token.enums.TokenSource.HUBSPOT;
import static instrumental.kiwi.security.token.enums.TokenSource.SYSTEM;
import static instrumental.kiwi.security.token.enums.TokenType.BEARER;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Token")
@Table(name = "tokens")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "token_id", updatable = false, nullable = false)
    private UUID tokenId;

    @Enumerated(EnumType.STRING)
    @Column(name = "source", updatable = false, nullable = false)
    private TokenSource source;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", updatable = false, nullable = false)
    private TokenType type;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "expiration_time")
    private LocalDateTime expirationTime;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "revoked", nullable = false)
    private boolean revoked;

    @Column(name = "expired", nullable = false)
    private boolean expired;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", updatable = false, nullable = false)
    private User user;

    private Token(String token, User user, TokenSource tokenSource) {

        this.source = tokenSource;
        this.type = BEARER;
        this.token = token;
        this.revoked = false;
        this.expired = false;
        this.user = user;
    }

    // TODO: TEST RESOLVE TOKEN FLOW

    private Token(String token, LocalDateTime expirationTime, String refreshToken, User user, TokenSource tokenSource) {

        this.source = tokenSource;
        this.type = BEARER;
        this.token = token;
        this.expirationTime = expirationTime;
        this.refreshToken = refreshToken;
        this.revoked = false;
        this.expired = false;
        this.user = user;
    }

    public static Token buildForSystem(String token, User user) {
        return new Token(token, user, SYSTEM);
    }

    public static Token buildForHubspot(String token, LocalDateTime expirationTime, String refreshToken, User user) {
        return new Token(token, expirationTime, refreshToken, user, HUBSPOT);
    }
}