package instrumental.kiwi.order.api;

import instrumental.kiwi.order.request.OrderRequest;
import instrumental.kiwi.order.service.OrderService;
import instrumental.kiwi.response.annotation.ApiResponder;
import instrumental.kiwi.security.annotation.WithRateLimitProtection;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Tag(name = "Order")
@RequiredArgsConstructor
@RequestMapping("kiwi/api/v1/orders/")
public class OrderController {

    private final OrderService orderService;

    @ApiResponder
    @WithRateLimitProtection
    @PostMapping(path = "place")
    public Map<String, Object> addOrder(@Validated @RequestBody OrderRequest orderRequest) {
        return orderService.placeOrder(orderRequest);
    }
}