package instrumental.kiwi.security.auth.api;

import instrumental.kiwi.response.annotation.ApiResponder;
import instrumental.kiwi.security.annotation.WithRateLimitProtection;
import instrumental.kiwi.security.auth.request.LoginRequest;
import instrumental.kiwi.security.auth.request.RegisterStoreRequest;
import instrumental.kiwi.security.auth.request.RegisterCustomerRequest;
import instrumental.kiwi.security.auth.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Tag(name = "Auth")
@RequiredArgsConstructor
@RequestMapping("kiwi/api/v1/auth/")
public class AuthController {

    private final AuthService authService;

    @ApiResponder
    @WithRateLimitProtection
    @PostMapping(path = "login")
    public String login(@Validated @RequestBody LoginRequest loginRequest, HttpServletResponse httpServletResponse) {
        return authService.login(loginRequest, httpServletResponse);
    }

    @ApiResponder
    @WithRateLimitProtection
    @PostMapping(path = "register-store")
    public Map<String, Object> registerStore(@Validated @RequestBody RegisterStoreRequest registerStoreRequest) {
        return authService.registerStore(registerStoreRequest);
    }

    @ApiResponder
    @WithRateLimitProtection
    @PostMapping(path = "register-customer")
    public Map<String, Object> registerCustomer(
            @Validated @RequestBody RegisterCustomerRequest registerCustomerRequest) {
        return authService.registerCustomer(registerCustomerRequest);
    }
}
