package instrumental.kiwi.product.api;

import instrumental.kiwi.product.request.ProductRequest;
import instrumental.kiwi.product.service.ProductService;
import instrumental.kiwi.security.annotation.WithRateLimitProtection;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("kiwi/api/v1/products/")
public class ProductController {

    private final ProductService productService;

    @WithRateLimitProtection
    @PostMapping(path = "add")
    public Map<String, Object> addProduct(@Validated @RequestBody ProductRequest productRequest) {
        return productService.addProduct(productRequest);
    }

    @GetMapping(path = "get-by-store/{store-id}/{page-size}")
    public String getAllProductsByStore(
            @PathVariable("store-id") Long storeId,
            @PathVariable("page-size") Integer pageSize
    ) {
        return productService.getAllProductsByStore(storeId, pageSize);
    }
}