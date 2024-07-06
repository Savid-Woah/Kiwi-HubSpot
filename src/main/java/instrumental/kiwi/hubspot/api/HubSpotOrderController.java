package instrumental.kiwi.hubspot.api;

import instrumental.kiwi.hubspot.provider.order.HubSpotOrderProvider;
import instrumental.kiwi.order.request.OrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("hubspot-buddy/api/v1/orders/")
public class HubSpotOrderController {

    private final HubSpotOrderProvider hubSpotOrderProvider;

    @PostMapping
    public String postOrder(@Validated @RequestBody OrderRequest orderRequest) {
        return null;
    }
}