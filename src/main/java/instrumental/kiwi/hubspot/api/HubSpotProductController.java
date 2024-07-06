package instrumental.kiwi.hubspot.api;

import instrumental.kiwi.hubspot.provider.product.HubSpotProductProvider;
import instrumental.kiwi.product.request.ProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("hubspot-buddy/api/v1/products/")
public class HubSpotProductController {

    private final HubSpotProductProvider hubSpotProductProvider;

    @PostMapping
    public String postProduct(@Validated @RequestBody ProductRequest productRequest) {
        return hubSpotProductProvider.postProduct(productRequest);
    }
}