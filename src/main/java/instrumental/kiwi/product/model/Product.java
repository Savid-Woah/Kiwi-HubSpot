package instrumental.kiwi.product.model;

import instrumental.kiwi.store.model.Store;
import instrumental.kiwi.contact.model.Contact;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Product")
@Table(name = "products")
public class Product {

    @Id
    @Column(name = "product_id", updatable = false, nullable = false)
    private Long productId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "hs_sku", nullable = false)
    private String hsSkU;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "hs_cost_of_goods_sold", nullable = false)
    private String hsCostOfGoodsSold;

    @Column(name = "hs_recurring_billing_period", nullable = false)
    private String hsRecurringBillingPeriod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id")
    private Contact contact;
}