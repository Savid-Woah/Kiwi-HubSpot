package instrumental.kiwi.contact.model;

import instrumental.kiwi.store.model.Store;
import instrumental.kiwi.contact.enums.ContactType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Contact")
@Table(name = "contacts")
public class Contact {

    @Id
    @Column(name = "contact_id", updatable = false, nullable = false)
    private Long contactId;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "company", nullable = false)
    private String company;

    @Column(name = "website", nullable = false)
    private String website;

    @Column(name = "lifecyclestage", nullable = false)
    private String lifecyclestage;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ContactType type;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id", updatable = false, nullable = false)
    private Store store;
}