package instrumental.kiwi.email;

import instrumental.kiwi.product.model.Product;
import instrumental.kiwi.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("hubspot-buddy/api/v1/emails/")
public class EmailController {

    private final EmailService emailService;
    private final ProductRepository productRepository;
    @PostMapping
    public void sendEmail() {
        Product product = productRepository.findById(0L).get();
        emailService.requestProductReplaceToSupplier(product, 1);
    }
}