package instrumental.kiwi.order.repository;

import instrumental.kiwi.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}