package instrumental.kiwi.product.service;

import instrumental.kiwi.contact.model.Contact;
import instrumental.kiwi.contact.repository.ContactRepository;
import instrumental.kiwi.exception.BackendException;
import instrumental.kiwi.hubspot.provider.product.HubSpotProductProvider;
import instrumental.kiwi.hubspot.provider.search.HubSpotSearchProvider;
import instrumental.kiwi.hubspot.provider.search.HubSpotSearchRequest;
import instrumental.kiwi.product.mapper.ProductDTOMapper;
import instrumental.kiwi.product.mapper.ProductHubSpotMapper;
import instrumental.kiwi.product.model.Product;
import instrumental.kiwi.product.repository.ProductRepository;
import instrumental.kiwi.product.request.ProductRequest;
import instrumental.kiwi.stock.request.StockData;
import instrumental.kiwi.stock.service.StockService;
import instrumental.kiwi.store.model.Store;
import instrumental.kiwi.store.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static instrumental.kiwi.exception.MsgCode.*;
import static instrumental.kiwi.hubspot.provider.search.HubSpotSearchRequest.buildForProductByStore;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private StockService stockService;
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ContactRepository contactRepository;
    @Mock
    private ProductHubSpotMapper productHubSpotMapper;
    @Mock
    private HubSpotSearchProvider hubSpotSearchProvider;
    @Mock
    private HubSpotProductProvider hubSpotProductProvider;
    @Spy
    private ProductDTOMapper productDTOMapper = new ProductDTOMapper();

    @InjectMocks
    private ProductService productService;

    private ProductRequest productRequest;
    private Product product;
    private Store store;
    private Contact contact;

    @BeforeEach
    void setUp() {

        store = new Store();
        store.setStoreId(1L);

        contact = new Contact();
        contact.setContactId(1L);

        product = new Product();
        product.setProductId(1L);
        product.setName("Product Name");

        StockData stockData = new StockData();
        stockData.setInitialStockQuantity(100);
        stockData.setNotificationTriggeringQuantity(10);

        productRequest = new ProductRequest();
        productRequest.setStoreId(1L);
        productRequest.setContactId(1L);
        productRequest.setStockData(stockData);
    }

    @Test
    @DisplayName(value = "Should get all products by store")
    void getAllProductsByStore() {

        // Given
        HubSpotSearchRequest searchRequest = buildForProductByStore(1L, 10);

        // When
        when(hubSpotSearchProvider.search(any(HubSpotSearchRequest.class))).thenReturn("searchResults");
        String result = productService.getAllProductsByStore(1L, 10);

        // Then
        verify(hubSpotSearchProvider, times(1)).search(searchRequest);
        assertEquals("searchResults", result);
    }

    @Test
    @DisplayName(value = "Should add product")
    void addProduct() {

        // When "mocked behaviour"
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));
        when(hubSpotProductProvider.postProduct(any(ProductRequest.class))).thenReturn("{}");
        when(productHubSpotMapper.apply("{}")).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        productService.addProduct(productRequest);

        // Then "expect"
        verify(storeRepository, times(1)).findById(1L);
        verify(contactRepository, times(1)).findById(1L);
        verify(hubSpotProductProvider, times(1)).postProduct(productRequest);
        verify(productHubSpotMapper, times(1)).apply("{}");
        verify(productRepository, times(1)).save(product);
        verify(stockService, times(1)).initProductStock(product, productRequest.getStockData());
    }

    @Test
    @DisplayName(value = "Should throw exception when store not found")
    void addProductShouldThrowBackendExceptionWhenStoreNotFound() {

        // When "mocked behaviour"
        when(storeRepository.findById(1L)).thenReturn(Optional.empty());

        // and "service called"
        BackendException exception = assertThrows(BackendException.class, () ->
                productService.addProduct(productRequest));

        // Then "expect"
        assertEquals(STORE_NOT_FOUND, exception.getMsgCode());
        verify(storeRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(productRepository, stockService, contactRepository);
    }

    @Test
    @DisplayName(value = "Should throw exception when contact not found")
    void addProductShouldThrowBackendExceptionWhenContactNotFound() {

        // When "mocked behaviour"
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(contactRepository.findById(1L)).thenReturn(Optional.empty());

        // and "service called"
        BackendException exception = assertThrows(BackendException.class, ()
                -> productService.addProduct(productRequest));

        // Then "expect"
        assertEquals(CONTACT_NOT_FOUND, exception.getMsgCode());
        verify(storeRepository, times(1)).findById(1L);
        verify(contactRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(productRepository, stockService);
    }

    @Test
    @DisplayName(value = "Should get product by id")
    void getProductById() {

        // When "mocked behaviour"
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // and "service called"
        Product result = productService.getProductById(1L);

        // Then "expect"
        verify(productRepository, times(1)).findById(1L);
        assertEquals(product, result);
    }

    @Test
    @DisplayName(value = "Should throw exception when product not found")
    void getProductByIdShouldThrowBackendExceptionWhenProductNotFound() {

        // When "mocked behaviour"
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // and "exception expected"
        BackendException exception = assertThrows(BackendException.class, ()
                -> productService.getProductById(1L));

        // Then "expect"
        assertEquals(PRODUCT_NOT_FOUND, exception.getMsgCode());
        verify(productRepository, times(1)).findById(1L);
    }
}
