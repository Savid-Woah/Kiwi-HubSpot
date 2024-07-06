package instrumental.kiwi.stock.service;

import instrumental.kiwi.exception.BackendException;
import instrumental.kiwi.product.model.Product;
import instrumental.kiwi.product.repository.ProductRepository;
import instrumental.kiwi.stock.dto.StockDTO;
import instrumental.kiwi.stock.mapper.StockDTOMapper;
import instrumental.kiwi.stock.model.Stock;
import instrumental.kiwi.stock.repository.StockRepository;
import instrumental.kiwi.stock.request.StockData;
import instrumental.kiwi.stock.request.StockRefillRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static instrumental.kiwi.exception.MsgCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Spy
    private StockDTOMapper stockDTOMapper = new StockDTOMapper();
    @Mock
    private StockRepository stockRepository;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private StockService stockService;

    private Stock stock;
    private Product product;
    private StockData stockData;

    @BeforeEach
    void setUp() {

        product = new Product();
        product.setProductId(1L);

        stockData = new StockData();
        stockData.setInitialStockQuantity(100);
        stockData.setNotificationTriggeringQuantity(10);

        stock = new Stock(product, stockData);
    }

    @Test
    @DisplayName(value = "Should init product stock")
    void initProductStock() {

        // When "service called"
        stockService.initProductStock(product, stockData);

        // Then
        verify(stockRepository, times(1)).save(any(Stock.class));
    }

    @Test
    @DisplayName(value = "Should lessen product stock")
    void lessenProductStock() {

        // When "mocked behaviour"
        when(stockRepository.findByProduct(product)).thenReturn(Optional.of(stock));

        // and "service called"
        stockService.lessenProductStock(product, 10);

        // Then
        verify(stockRepository, times(1)).findByProduct(product);
        verify(stockRepository, times(1)).save(stock);
        assertEquals(90, stock.getQuantity());
    }

    @Test
    @DisplayName(value = "Should place order")
    void lessenProductStockShouldThrowBackendExceptionWhenStockNotFound() {

        // When "mocked behaviour"
        when(stockRepository.findByProduct(product)).thenReturn(Optional.empty());

        // and "expected exception"
        BackendException exception = assertThrows(BackendException.class, ()
                -> stockService.lessenProductStock(product, 10));

        // Then
        assertEquals(STOCK_NOT_FOUND, exception.getMsgCode());
        verify(stockRepository, times(1)).findByProduct(product);
        verify(stockRepository, never()).save(any(Stock.class));
    }

    @Test
    @DisplayName(value = "Should get stock by product")
    void getStockByProduct() {

        // When "mocked behaviour"
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(stockRepository.findByProduct(product)).thenReturn(Optional.of(stock));
        StockDTO stockDTO = stockDTOMapper.apply(stock);
        when(stockDTOMapper.apply(stock)).thenReturn(stockDTO);

        // and "service called"
        Optional<StockDTO> result = stockService.getStockByProduct(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(stockDTO, result.get());
        verify(productRepository, times(1)).findById(1L);
        verify(stockRepository, times(1)).findByProduct(product);

    }

    @Test
    @DisplayName(value = "Should throw exception when product not found")
    void getStockByProductShouldThrowBackendExceptionWhenProductNotFound() {

        // When "mocked behaviour"
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // and "expected exception"
        BackendException exception = assertThrows(BackendException.class, ()
                -> stockService.getStockByProduct(1L));

        // Then
        assertEquals(PRODUCT_NOT_FOUND, exception.getMsgCode());
        verify(productRepository, times(1)).findById(1L);
        verify(stockRepository, never()).findByProduct(any(Product.class));
    }

    @Test
    @DisplayName("Should refill product stock")
    void refillProductStock() {

        // Given
        UUID stockId = UUID.fromString("b32dd553-cc9f-4212-86a8-4135b2386e96");
        StockRefillRequest stockRefillRequest = new StockRefillRequest();
        stockRefillRequest.setStockId(stockId);
        stockRefillRequest.setQuantity(50);

        // When
        when(stockRepository.findById(stockId)).thenReturn(Optional.of(stock));

        stockService.refillProductStock(stockRefillRequest);

        // Then
        verify(stockRepository, times(1)).findById(stockId);
        verify(stockRepository, times(1)).save(stock);
        assertEquals(150, stock.getQuantity());
    }

    @Test
    @DisplayName("Should throw exception when stock not found during refill")
    void refillProductStockShouldThrowBackendExceptionWhenStockNotFound() {

        // Given
        UUID stockId = UUID.fromString("b32dd553-cc9f-4212-86a8-4135b2386e96");
        StockRefillRequest stockRefillRequest = new StockRefillRequest();
        stockRefillRequest.setStockId(stockId);
        stockRefillRequest.setQuantity(50);

        // When
        when(stockRepository.findById(stockId)).thenReturn(Optional.empty());


        BackendException exception = assertThrows(BackendException.class, () ->
                stockService.refillProductStock(stockRefillRequest));

        // Then
        assertEquals(STOCK_NOT_FOUND, exception.getMsgCode());
        verify(stockRepository, times(1)).findById(stockId);
        verify(stockRepository, never()).save(any(Stock.class));
    }
}
