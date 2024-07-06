package instrumental.kiwi.email;

import instrumental.kiwi.order.model.Order;
import instrumental.kiwi.product.model.Product;
import instrumental.kiwi.stock.model.Stock;

public interface EmailProvider {
    void sendOrderBillEmail(Order order);
    void sendStockRequestEmail(Stock stock);
    void sendTicketClaimEmail(Product product, Integer quantity);
}