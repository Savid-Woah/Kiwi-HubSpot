package instrumental.kiwi.customer.mapper;

import instrumental.kiwi.customer.dto.CustomerDTO;
import instrumental.kiwi.customer.model.Customer;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class CustomerDTOMapper implements Function<Customer, CustomerDTO> {

    @Override
    public CustomerDTO apply(Customer customer) {

        return new CustomerDTO(

                customer.getCustomerId()
        );
    }
}
