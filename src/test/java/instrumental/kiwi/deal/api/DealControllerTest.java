package instrumental.kiwi.deal.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import instrumental.kiwi.deal.service.DealService;
import instrumental.kiwi.exception.MessageTextResolver;
import instrumental.kiwi.security.auth.service.AuthService;
import instrumental.kiwi.security.config.filter.JwtAuthenticationFilter;
import instrumental.kiwi.security.config.service.JwtService;
import instrumental.kiwi.security.config.service.OAuth2AuthenticationSuccessHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DealController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class DealControllerTest {

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
    private DealService dealService;

    @Test
    @DisplayName("Should return deals by store ID and page size")
    void testGetAllDealsByStore() throws Exception {

        // Given
        Long storeId = 1L;
        Integer pageSize = 10;

        // When "mocked behaviour"
        when(dealService.getAllDealsByStore(Mockito.eq(storeId), Mockito.eq(pageSize)))
                .thenReturn("{}");

        // Then
        mockMvc.perform(get("/kiwi/api/v1/deals/get-by-store/{store-id}/{page-size}", storeId, pageSize))
                .andExpect(status().isOk());
    }
}