package instrumental.kiwi.order.model;

import instrumental.kiwi.customer.model.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Order")
@Table(name = "orders")
public class Order {

    @Id
    @Column(name = "order_id", updatable = false, nullable = false)
    private Long orderId;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "hs_order_name")
    private String hsOrderName;

    @Column(name = "hs_currency_code")
    private String hsCurrencyCode;

    @Column(name = "hs_source_store")
    private String hsSourceStore;

    @Column(name = "hs_fulfillment_status")
    private String hsFulfillmentStatus;

    @Column(name = "hs_shipping_address_city")
    private String hsShippingAddressCity;

    @Column(name = "hs_shipping_address_state")
    private String hsShippingAddressState;

    @Column(name = "hs_shipping_address_street")
    private String hsShippingAddressStreet;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", updatable = false, nullable = false)
    private Customer customer;
}