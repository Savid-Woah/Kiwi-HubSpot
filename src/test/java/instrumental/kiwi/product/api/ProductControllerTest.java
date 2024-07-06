package instrumental.kiwi.product.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import instrumental.kiwi.exception.MessageTextResolver;
import instrumental.kiwi.product.request.ProductRequest;
import instrumental.kiwi.product.service.ProductService;
import instrumental.kiwi.security.auth.service.AuthService;
import instrumental.kiwi.security.config.filter.JwtAuthenticationFilter;
import instrumental.kiwi.security.config.service.JwtService;
import instrumental.kiwi.security.config.service.OAuth2AuthenticationSuccessHandler;
import instrumental.kiwi.stock.request.StockData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AuthService authService;
    @MockBean
    private MessageTextResolver messageTextResolver;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private OAuth2AuthenticationSuccessHandler auth2AuthenticationSuccessHandler;
    @MockBean
    private ProductService productService;

    @Test
    @DisplayName("Should add a product")
    void shouldAddProduct() throws Exception {

        // Given
        StockData stockData = new StockData();
        stockData.setRefillQuantity(100);
        stockData.setInitialStockQuantity(12);
        stockData.setNotificationTriggeringQuantity(10);

        ProductRequest productRequest = ProductRequest
                .builder()
                .name("Test Product")
                .price(100.0)
                .hsSku("TEST_SKU")
                .description("Test product description")
                .hsCostOfGoodsSold("100")
                .hsRecurringBillingPeriod("Monthly")
                .stockData(stockData)
                .storeId(1L)
                .contactId(1L)
                .build();

        // When "mocked behaviour"
        when(productService.addProduct(any(ProductRequest.class))).thenReturn(anyMap());

        // and "endpoint hit"
        mockMvc.perform(post("/kiwi/api/v1/products/add")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isOk());

        // Then "expect"
        verify(productService, Mockito.times(1)).addProduct(productRequest);
    }

    @Test
    @DisplayName("Should get all products by store ID")
    void shouldGetAllProductsByStoreId() throws Exception {

        // Given
        Long storeId = 1L;
        Integer pageSize = 10;

        // When "mocked behaviour"
        when(productService.getAllProductsByStore(storeId, pageSize)).thenReturn("Mocked products response");

        // and "endpoint hit"
        mockMvc.perform(MockMvcRequestBuilders.get("/kiwi/api/v1/products/get-by-store/{store-id}/{page-size}", storeId, pageSize))
                .andExpect(status().isOk())
                .andExpect(content().string("Mocked products response"));

        // Then "expect"
        verify(productService, Mockito.times(1)).getAllProductsByStore(storeId, pageSize);
    }
}