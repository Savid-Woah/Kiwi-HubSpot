package instrumental.kiwi.security.token.repository;

import instrumental.kiwi.security.token.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static instrumental.kiwi.security.token.query.TokenQuery.FIND_ALL_VALID_TOKENS_BY_USER;
import static instrumental.kiwi.security.token.query.TokenQuery.FIND_VALID_HUBSPOT_ACCESS_TOKEN_BY_USER;
import static instrumental.kiwi.security.token.query.TokenQueryParam.USER_ID;

public interface TokenRepository extends JpaRepository<Token, UUID> {
    @Query(FIND_ALL_VALID_TOKENS_BY_USER)
    List<Token> findAllValidTokensByUser(@Param(USER_ID) UUID userId);
    Optional<Token> findByToken(String token);
    @Query(FIND_VALID_HUBSPOT_ACCESS_TOKEN_BY_USER)
    Optional<Token> findHubspotAccessTokenByUser(@Param(USER_ID) UUID userId);
    Optional<Token> findByRefreshToken(String refreshToken);
}