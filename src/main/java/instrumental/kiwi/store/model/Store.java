package instrumental.kiwi.store.model;

import instrumental.kiwi.store.request.StoreRequest;
import instrumental.kiwi.security.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Store")
@Table(name = "stores")
public class Store {

    @Id
    @Column(name = "store_id", updatable = false, nullable = false)
    private Long storeId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "domain", nullable = false)
    private String domain;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "industry", nullable = false)
    private String industry;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "life_cycle_state", nullable = false)
    private String lifeCycleStage;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", updatable = false, nullable = false)
    private User user;

    public Store(Long storeId, StoreRequest storeRequest, User user) {

        this.storeId = storeId;
        this.name = storeRequest.getName();
        this.domain = storeRequest.getDomain();
        this.city = storeRequest.getCity();
        this.industry = storeRequest.getIndustry();
        this.phoneNumber = storeRequest.getPhoneNumber();
        this.state = storeRequest.getState();
        this.lifeCycleStage = storeRequest.getLifeCycleStage();
        this.user = user;
    }
}