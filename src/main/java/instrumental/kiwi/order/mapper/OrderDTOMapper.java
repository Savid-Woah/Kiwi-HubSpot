package instrumental.kiwi.order.mapper;

import instrumental.kiwi.order.dto.OrderDTO;
import instrumental.kiwi.order.model.Order;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class OrderDTOMapper implements Function<Order, OrderDTO> {

    @Override
    public OrderDTO apply(Order order) {

        return new OrderDTO(

                order.getOrderId()
        );
    }
}