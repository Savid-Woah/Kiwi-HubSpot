package instrumental.kiwi.security.token.query;

public class TokenQuery {
    public static final String FIND_ALL_VALID_TOKENS_BY_USER =
            """
            select t from Token t inner join User u on t.user.userId = u.userId
            where u.userId = :userId and (t.revoked = false and t.expired = false)
            """;

    public static final String FIND_VALID_HUBSPOT_ACCESS_TOKEN_BY_USER =
            """
            select t
            from Token t
            inner join User u on t.user.userId = u.userId
            where u.userId = :userId
            and (t.revoked = false and t.expired = false)
            and t.source = 'HUBSPOT'
            """;
}