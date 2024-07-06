package instrumental.kiwi.email;

import instrumental.kiwi.order.model.Order;
import instrumental.kiwi.product.model.Product;
import instrumental.kiwi.stock.model.Stock;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailService {

    private final EmailProvider emailProvider;

    public void sendBillToCustomer(Order order) {
        emailProvider.sendOrderBillEmail(order);
    }

    public void requestStockToSupplier(Stock stock) {
        emailProvider.sendStockRequestEmail(stock);
    }

    public void requestProductReplaceToSupplier(Product product, Integer quantity) {
        emailProvider.sendTicketClaimEmail(product, quantity);
    }
}