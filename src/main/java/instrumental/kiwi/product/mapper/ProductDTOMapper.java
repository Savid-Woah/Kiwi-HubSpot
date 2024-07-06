package instrumental.kiwi.product.mapper;

import instrumental.kiwi.product.dto.ProductDTO;
import instrumental.kiwi.product.model.Product;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ProductDTOMapper implements Function<Product, ProductDTO> {

    @Override
    public ProductDTO apply(Product product) {

        return new ProductDTO(

                product.getProductId()
        );
    }
}