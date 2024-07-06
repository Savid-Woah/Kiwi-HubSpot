package instrumental.kiwi.customer.api;

import instrumental.kiwi.customer.dto.CustomerDTO;
import instrumental.kiwi.customer.service.CustomerService;
import instrumental.kiwi.security.annotation.WithRateLimitProtection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "kiwi/api/v1/customers/")
public class CustomerController {

    private final CustomerService customerService;

    @WithRateLimitProtection
    @GetMapping(path = "get-by-store/{customer-id}/{page-number}/{page-size}")
    public Page<CustomerDTO> getAllCustomersByStore(
            @PathVariable("customer-id") Long storeId,
            @PathVariable("page-number") Integer pageNumber,
            @PathVariable("page-size") Integer pageSize
    ) {
        return customerService.getAllCustomersByStore(storeId, pageNumber, pageSize);
    }
}