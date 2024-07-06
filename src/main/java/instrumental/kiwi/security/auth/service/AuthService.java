package instrumental.kiwi.security.auth.service;

import instrumental.kiwi.customer.dto.CustomerDTO;
import instrumental.kiwi.customer.request.CustomerRequest;
import instrumental.kiwi.customer.service.CustomerService;
import instrumental.kiwi.exception.BackendException;
import instrumental.kiwi.response.Response;
import instrumental.kiwi.security.auth.request.LoginRequest;
import instrumental.kiwi.security.auth.request.RegisterCustomerRequest;
import instrumental.kiwi.security.auth.request.RegisterStoreRequest;
import instrumental.kiwi.security.config.service.JwtService;
import instrumental.kiwi.security.user.model.User;
import instrumental.kiwi.security.user.request.UserRequest;
import instrumental.kiwi.security.user.service.UserService;
import instrumental.kiwi.store.dto.StoreDTO;
import instrumental.kiwi.store.request.StoreRequest;
import instrumental.kiwi.store.service.StoreService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import static instrumental.kiwi.exception.MsgCode.OOPS_ERROR;
import static instrumental.kiwi.response.message.ResponseMessage.CUSTOMER_REGISTERED;
import static instrumental.kiwi.response.message.ResponseMessage.STORE_REGISTERED;
import static instrumental.kiwi.response.utils.ResponseUtils.generateResponse;
import static instrumental.kiwi.security.user.enums.Role.CUSTOMER;
import static instrumental.kiwi.security.user.enums.Role.STORE;
import static org.springframework.http.HttpStatus.CREATED;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final UserService userService;
    private final CookieService cookieService;
    private final StoreService storeService;
    private final CustomerService customerService;
    private final AuthenticationManager authenticationManager;

    public String login(LoginRequest loginRequest, HttpServletResponse httpServletResponse) {

        try {

            String email = loginRequest.getEmail();
            String password = loginRequest.getPassword();
            User user = userService.getUserByEmail(email).orElseThrow(() -> new BackendException(OOPS_ERROR));
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);
            authenticationManager.authenticate(authToken);
            String jwt = jwtService.generateToken(user);
            cookieService.addCookie(httpServletResponse, "jwt", jwt, -1);
            jwtService.saveUserToken(user, jwt);
            return jwt;

        } catch (AuthenticationException exception) {
            throw new BackendException(OOPS_ERROR);
        }
    }

    public Response registerStore(RegisterStoreRequest registerStoreRequest) {

        UserRequest userRequest = registerStoreRequest.getUserRequest();
        StoreRequest storeRequest = registerStoreRequest.getStoreRequest();
        User user = userService.createSytemUser(userRequest, STORE);
        StoreDTO storeDTO = storeService.createStore(storeRequest, user);
        return generateResponse(storeDTO, CREATED, STORE_REGISTERED);
    }

    public Response registerCustomer(RegisterCustomerRequest registerCustomerRequest) {

        UserRequest userRequest = registerCustomerRequest.getUserRequest();
        CustomerRequest customerRequest = registerCustomerRequest.getCustomerRequest();
        User user = userService.createSytemUser(userRequest, CUSTOMER);
        CustomerDTO customerDTO = customerService.createCustomer(customerRequest, user);

        return generateResponse(customerDTO, CREATED, CUSTOMER_REGISTERED);
    }
}