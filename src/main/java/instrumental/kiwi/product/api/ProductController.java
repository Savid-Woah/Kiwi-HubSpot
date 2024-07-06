package instrumental.kiwi.product.api;

import instrumental.kiwi.product.request.ProductRequest;
import instrumental.kiwi.product.service.ProductService;
import instrumental.kiwi.response.Response;
import instrumental.kiwi.response.annotation.ApiResponder;
import instrumental.kiwi.security.annotation.WithRateLimitProtection;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Product")
@RequiredArgsConstructor
@RequestMapping("kiwi/api/v1/products/")
public class ProductController {

    private final ProductService productService;

    @ApiResponder
    @WithRateLimitProtection
    @PostMapping(path = "add")
    public Response addProduct(@Validated @RequestBody ProductRequest productRequest) {
        return productService.addProduct(productRequest);
    }

    @ApiResponder
    @GetMapping(path = "get-by-store/{store-id}/{page-size}")
    public String getAllProductsByStore(
            @PathVariable("store-id") Long storeId,
            @PathVariable("page-size") Integer pageSize
    ) {
        return productService.getAllProductsByStore(storeId, pageSize);
    }
}