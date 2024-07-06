package instrumental.kiwi.product.repository;

import instrumental.kiwi.store.model.Store;
import instrumental.kiwi.product.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByStore(Store store, Pageable pageable);
}