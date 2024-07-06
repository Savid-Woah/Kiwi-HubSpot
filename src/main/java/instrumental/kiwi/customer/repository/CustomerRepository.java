package instrumental.kiwi.customer.repository;

import instrumental.kiwi.store.model.Store;
import instrumental.kiwi.customer.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Page<Customer> findAllByStore(Store store, Pageable pageable);
}