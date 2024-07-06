package instrumental.kiwi.customer.model;

import instrumental.kiwi.customer.request.CustomerRequest;
import instrumental.kiwi.security.user.model.User;
import instrumental.kiwi.store.model.Store;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Customer")
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "customer_id", updatable = false, nullable = false)
    private Long customerId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", updatable = false, nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id", updatable = false, nullable = false)
    private Store store;

    public Customer(CustomerRequest customerRequest, User user) {

        this.name = customerRequest.getName();
        this.lastName = customerRequest.getLastName();
        this.phoneNumber = customerRequest.getPhoneNumber();
        this.user = user;
    }
}