package instrumental.kiwi.stock.service;

import instrumental.kiwi.email.EmailService;
import instrumental.kiwi.exception.BackendException;
import instrumental.kiwi.product.model.Product;
import instrumental.kiwi.product.repository.ProductRepository;
import instrumental.kiwi.stock.dto.StockDTO;
import instrumental.kiwi.stock.mapper.StockDTOMapper;
import instrumental.kiwi.stock.model.Stock;
import instrumental.kiwi.stock.repository.StockRepository;
import instrumental.kiwi.stock.request.StockData;
import instrumental.kiwi.stock.request.StockRefillRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.BiConsumer;

import static instrumental.kiwi.exception.MsgCode.PRODUCT_NOT_FOUND;
import static instrumental.kiwi.exception.MsgCode.STOCK_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class StockService {

    @Autowired
    private EmailService emailService;
    private final StockDTOMapper stockDTOMapper;
    private final StockRepository stockRepository;
    private final ProductRepository productRepository;

    public void initProductStock(Product product, StockData stockData) {

        Stock stock = new Stock(product, stockData);
        stockRepository.save(stock);
    }

    public void lessenProductStock(Product product, Integer quantity) {

        Stock stock = stockRepository.findByProduct(product).orElseThrow(()
                -> new BackendException(STOCK_NOT_FOUND));

        lessenStockQuantity.accept(stock, quantity);
        stockRepository.save(stock);
    }

    public void refillProductStock(StockRefillRequest stockRefillRequest) {

        Stock stock = stockRepository.findById(stockRefillRequest.getStockId())
                .orElseThrow(() -> new BackendException(STOCK_NOT_FOUND));

        refillStockQuantity.accept(stock, stockRefillRequest.getQuantity());
        stockRepository.save(stock);
    }

    public Optional<StockDTO> getStockByProduct(Long productId) {

        Product product = productRepository.findById(productId).orElseThrow(()
                -> new BackendException(PRODUCT_NOT_FOUND));

        return stockRepository.findByProduct(product)
                .map(stockDTOMapper);
    }

    /**
     * Email Service is field-injected via @Autowired so
     * that it can be recognized by the BiConsumer
     */
    private final BiConsumer<Stock, Integer> lessenStockQuantity = (stock, quantity) -> {

        Integer currentQuantity = stock.getQuantity();
        Integer updatedQuantity = currentQuantity - quantity;
        Integer notificationTriggeringQuantity = stock.getNotificationTriggeringQuantity();
        stock.setQuantity(updatedQuantity);

        if(updatedQuantity <= notificationTriggeringQuantity) {
            emailService.requestStockToSupplier(stock);
        }
    };

    private final BiConsumer<Stock, Integer> refillStockQuantity = (stock, quantity) -> {

        Integer currentQuantity = stock.getQuantity();
        Integer updatedQuantity = currentQuantity + quantity;
        stock.setQuantity(updatedQuantity);
    };
}