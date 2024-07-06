package instrumental.kiwi.store.service;

import instrumental.kiwi.hubspot.provider.company.HubSpotCompanyProvider;
import instrumental.kiwi.store.dto.StoreDTO;
import instrumental.kiwi.store.mapper.StoreDTOMapper;
import instrumental.kiwi.store.model.Store;
import instrumental.kiwi.store.repository.StoreRepository;
import instrumental.kiwi.store.request.StoreRequest;
import instrumental.kiwi.security.user.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreService {

    private final StoreDTOMapper storeDTOMapper;
    private final StoreRepository storeRepository;
    private final HubSpotCompanyProvider hubSpotCompanyProvider;

    public StoreDTO createStore(StoreRequest storeRequest, User user) {

        String createdCompany = hubSpotCompanyProvider.postCompany(storeRequest);
        String createdCompanyId = hubSpotCompanyProvider.extractCompanyId(createdCompany);
        Store store = buildStore(createdCompanyId, storeRequest, user);
        Store savedStore = storeRepository.save(store);
        return storeDTOMapper.apply(savedStore);
    }

    private Store buildStore(String createdCompanyId, StoreRequest storeRequest, User user) {
        return new Store(Long.parseLong(createdCompanyId), storeRequest, user);
    }
}