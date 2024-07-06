package instrumental.kiwi.stock.model;

import instrumental.kiwi.product.model.Product;
import instrumental.kiwi.stock.request.StockData;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Stock")
@Table(name = "stock")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "stock_id", updatable = false, nullable = false)
    private UUID stockId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "refill_quantity", nullable = false)
    private Integer refillQuantity;

    @Column(name = "notification_triggering_quantity", nullable = false)
    private Integer notificationTriggeringQuantity;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", updatable = false, nullable = false)
    private Product product;

    public Stock(Product product, StockData stockData) {

        this.product = product;
        this.quantity = stockData.getInitialStockQuantity();
        this.refillQuantity = stockData.getRefillQuantity();
        this.notificationTriggeringQuantity = stockData.getNotificationTriggeringQuantity();
    }
}