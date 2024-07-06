package instrumental.kiwi.contact.repository;

import instrumental.kiwi.store.model.Store;
import instrumental.kiwi.contact.model.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    Page<Contact> findAllByStore(Store store, Pageable pageable);
}