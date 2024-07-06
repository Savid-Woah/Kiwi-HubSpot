package instrumental.kiwi.stock.repository;

import instrumental.kiwi.product.model.Product;
import instrumental.kiwi.stock.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StockRepository extends JpaRepository<Stock, UUID> {
    Optional<Stock> findByProduct(Product product);
}