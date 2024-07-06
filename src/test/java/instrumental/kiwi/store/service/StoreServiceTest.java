package instrumental.kiwi.store.service;

import instrumental.kiwi.hubspot.provider.company.HubSpotCompanyProvider;
import instrumental.kiwi.store.dto.StoreDTO;
import instrumental.kiwi.store.mapper.StoreDTOMapper;
import instrumental.kiwi.store.model.Store;
import instrumental.kiwi.store.repository.StoreRepository;
import instrumental.kiwi.store.request.StoreRequest;
import instrumental.kiwi.security.user.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;
    @Mock
    private HubSpotCompanyProvider hubSpotCompanyProvider;
    @Spy
    private StoreDTOMapper storeDTOMapper = new StoreDTOMapper();
    @InjectMocks
    private StoreService storeService;

    @Test
    @DisplayName(value = "Should create store")
    void createStore() {

        // Given "a store request"
        StoreRequest storeRequest = StoreRequest.builder()
                .name("Test Store")
                .domain("test.com")
                .city("Test City")
                .industry("Test Industry")
                .phoneNumber("1234567890")
                .state("Test State")
                .lifeCycleStage("Test Lifecycle")
                .build();

        // and "a mocked user"
        User user = Mockito.mock(User.class);

        // and "responses expected"
        String createdCompany = "{}";
        String createdCompanyId = "12345";

        // When "mocked behaviour"
        when(hubSpotCompanyProvider.postCompany(any(StoreRequest.class))).thenReturn(createdCompany);
        when(hubSpotCompanyProvider.extractCompanyId(createdCompany)).thenReturn(createdCompanyId);

        // Build the expected store object that will be saved
        Store savedStore = buildStore(storeRequest, user);

        // Mock the repository save behavior
        when(storeRepository.save(any(Store.class))).thenReturn(savedStore);

        // Create a mock StoreDTO
        StoreDTO storeDTO = new StoreDTO(1L, savedStore.getName(), savedStore.getPhoneNumber());

        // Mock the mapper behavior
        doReturn(storeDTO).when(storeDTOMapper).apply(savedStore);

        // Call the service
        StoreDTO result = storeService.createStore(storeRequest, user);

        // then "verify"
        verify(hubSpotCompanyProvider, times(1)).postCompany(storeRequest);
        verify(hubSpotCompanyProvider, times(1)).extractCompanyId(createdCompany);
        verify(storeRepository, times(1)).save(any(Store.class));
        verify(storeDTOMapper, times(1)).apply(savedStore);

        // Assert the result
        assertNotNull(result);
        assertEquals(1L, result.storeId());
        assertEquals(storeRequest.getName(), result.name());
        assertEquals(storeRequest.getPhoneNumber(), result.phone());
    }

    private Store buildStore(StoreRequest storeRequest, User user) {

        return Store.builder()
                .storeId(0L)
                .name(storeRequest.getName())
                .domain(storeRequest.getDomain())
                .city(storeRequest.getCity())
                .industry(storeRequest.getIndustry())
                .phoneNumber(storeRequest.getPhoneNumber())
                .state(storeRequest.getState())
                .lifeCycleStage(storeRequest.getLifeCycleStage())
                .user(user)
                .build();
    }
}
