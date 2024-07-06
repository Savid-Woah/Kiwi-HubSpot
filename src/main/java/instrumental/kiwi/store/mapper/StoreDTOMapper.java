package instrumental.kiwi.store.mapper;

import instrumental.kiwi.store.dto.StoreDTO;
import instrumental.kiwi.store.model.Store;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class StoreDTOMapper implements Function<Store, StoreDTO> {

    @Override
    public StoreDTO apply(Store store) {

        return new StoreDTO(

                store.getStoreId(),
                store.getName(),
                store.getPhoneNumber()
        );
    }
}