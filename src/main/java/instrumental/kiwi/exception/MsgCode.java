package instrumental.kiwi.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MsgCode {

    OOPS_ERROR(500, "oops-error"),
    USER_NOT_FOUND(404, "user-not-found"),
    STORE_NOT_FOUND(404, "store-not-found"),
    STOCK_NOT_FOUND(404, "stock-not-found"),
    ORDER_NOT_FOUND(404, "order-not-found"),
    CONTACT_NOT_FOUND(404, "contact-not-found"),
    PRODUCT_NOT_FOUND(404, "product-not-found"),
    CUSTOMER_NOT_FOUND(404, "customer-not-found"),
    NO_HUBSPOT_TOKEN_PRESENT(401, "no-hubspot-token-present");

    private final Integer code;
    private final String languageKey;
}