package instrumental.kiwi.store.repository;

import instrumental.kiwi.store.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
}