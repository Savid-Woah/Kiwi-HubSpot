package instrumental.kiwi.security.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {

    ADMIN_CREATE("ADMIN:CREATE"),
    ADMIN_READ("ADMIN:READ"),
    ADMIN_UPDATE("ADMIN:UPDATE"),
    ADMIN_DELETE("ADMIN:DELETE"),

    STORE_CREATE("STORE:CREATE"),
    STORE_READ("STORE:READ"),
    STORE_UPDATE("STORE:UPDATE"),
    STORE_DELETE("STORE:DELETE"),

    CUSTOMER_CREATE("COMPANY:CREATE"),
    CUSTOMER_READ("COMPANY:READ"),
    CUSTOMER_UPDATE("COMPANY:UPDATE"),
    CUSTOMER_DELETE("COMPANY:DELETE");

    private final String permissions;
}