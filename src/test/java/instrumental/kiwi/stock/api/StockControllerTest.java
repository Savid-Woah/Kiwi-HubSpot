package instrumental.kiwi.stock.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import instrumental.kiwi.exception.MessageTextResolver;
import instrumental.kiwi.security.auth.service.AuthService;
import instrumental.kiwi.security.config.filter.JwtAuthenticationFilter;
import instrumental.kiwi.security.config.service.JwtService;
import instrumental.kiwi.security.config.service.OAuth2AuthenticationSuccessHandler;
import instrumental.kiwi.stock.dto.StockDTO;
import instrumental.kiwi.stock.service.StockService;
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

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StockController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class StockControllerTest {

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
    private StockService stockService;

    @Test
    @DisplayName("Should return stock DTO by product ID")
    void shouldReturnStockDTOByProductID() throws Exception {

        // Given
        Long productId = 1L;
        StockDTO expectedStockDTO = new StockDTO(UUID.randomUUID());

        // When "service called"
        when(stockService.getStockByProduct(productId)).thenReturn(Optional.of(expectedStockDTO));

        // and "endpoint hit"
        mockMvc.perform(get("/kiwi/api/v1/stock/get-by-product/{product-id}", productId))
                .andExpect(status().isOk());

        // Then
        verify(stockService, Mockito.times(1)).getStockByProduct(productId);
    }
}
