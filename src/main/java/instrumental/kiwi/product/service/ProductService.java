package instrumental.kiwi.product.service;

import instrumental.kiwi.contact.model.Contact;
import instrumental.kiwi.contact.repository.ContactRepository;
import instrumental.kiwi.exception.BackendException;
import instrumental.kiwi.hubspot.provider.product.HubSpotProductProvider;
import instrumental.kiwi.hubspot.provider.search.HubSpotSearchProvider;
import instrumental.kiwi.hubspot.provider.search.HubSpotSearchRequest;
import instrumental.kiwi.product.dto.ProductDTO;
import instrumental.kiwi.product.mapper.ProductDTOMapper;
import instrumental.kiwi.product.mapper.ProductHubSpotMapper;
import instrumental.kiwi.product.model.Product;
import instrumental.kiwi.product.repository.ProductRepository;
import instrumental.kiwi.product.request.ProductRequest;
import instrumental.kiwi.stock.request.StockData;
import instrumental.kiwi.stock.service.StockService;
import instrumental.kiwi.store.model.Store;
import instrumental.kiwi.store.repository.StoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

import static instrumental.kiwi.exception.MsgCode.*;
import static instrumental.kiwi.hubspot.provider.search.HubSpotSearchRequest.buildForProductByStore;
import static instrumental.kiwi.response.handler.ResponseHandler.generateResponse;
import static instrumental.kiwi.response.message.ResponseMessage.PRODUCT_ADDED;
import static org.springframework.http.HttpStatus.CREATED;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final StockService stockService;
    private final StoreRepository storeRepository;
    private final ProductDTOMapper productDTOMapper;
    private final ProductRepository productRepository;
    private final ContactRepository contactRepository;
    private final ProductHubSpotMapper productHubSpotMapper;
    private final HubSpotSearchProvider hubSpotSearchProvider;
    private final HubSpotProductProvider hubSpotProductProvider;

    public String getAllProductsByStore(Long storeId, Integer pageSize) {

        HubSpotSearchRequest searchRequest = buildForProductByStore(storeId, pageSize);
        return hubSpotSearchProvider.search(searchRequest);
    }

    public Map<String, Object> addProduct(ProductRequest productRequest) {

        String createdHubspotProduct = hubSpotProductProvider.postProduct(productRequest);
        Product product = productHubSpotMapper.apply(createdHubspotProduct);
        setProductRelations(productRequest, product);

        Product savedProduct = productRepository.save(product);
        ProductDTO productDTO = productDTOMapper.apply(savedProduct);

        StockData stockData = productRequest.getStockData();
        stockService.initProductStock(savedProduct, stockData);

        return generateResponse(productDTO, CREATED, PRODUCT_ADDED);
    }

    private void setProductRelations(ProductRequest productRequest, Product product) {

        Long storeId = productRequest.getStoreId();
        Long contactId = productRequest.getContactId();

        Store store = storeRepository.findById(storeId).orElseThrow(()
                -> new BackendException(STORE_NOT_FOUND));

        Contact contact = contactRepository.findById(contactId).orElseThrow(()
                -> new BackendException(CONTACT_NOT_FOUND));

        product.setStore(store);
        product.setContact(contact);
    }

    public Product getProductById(Long productId) {

        return productRepository.findById(productId).orElseThrow(()
                -> new BackendException(PRODUCT_NOT_FOUND));
    }
}