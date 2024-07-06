package instrumental.kiwi.seeder;

import instrumental.kiwi.customer.model.Customer;
import instrumental.kiwi.customer.repository.CustomerRepository;
import instrumental.kiwi.product.model.Product;
import instrumental.kiwi.stock.model.Stock;
import instrumental.kiwi.store.model.Store;
import instrumental.kiwi.store.repository.StoreRepository;
import instrumental.kiwi.contact.model.Contact;
import instrumental.kiwi.contact.repository.ContactRepository;
import instrumental.kiwi.product.repository.ProductRepository;
import instrumental.kiwi.security.user.model.User;
import instrumental.kiwi.security.user.repository.UserRepository;
import instrumental.kiwi.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import static instrumental.kiwi.contact.enums.ContactType.SUPPLIER;
import static instrumental.kiwi.security.user.enums.Role.*;
import static instrumental.kiwi.security.user.enums.Source.SYSTEM;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final StockRepository stockRepository;
    private final StoreRepository storeRepository;
    private final ContactRepository contactRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        seedData();
    }

    private void seedData() {

        seedAdmin();
        Store store = seedStore();
        Contact contact = seedContact(store);
        Product product = seedProduct(store, contact);
        Customer customer = seedCustomer(store);
        seedStock(product);

        System.out.println("Store ID: " + store.getStoreId());
        System.out.println("Contact ID: " + contact.getContactId());
        System.out.println("Product ID: " + product.getProductId());
        System.out.println("Customer ID: " + customer.getCustomerId());
    }

    public void seedAdmin() {

        User user = User
                .builder()
                .email("admin@example.com")
                .password("$**9SBW$3&")
                .role(ADMIN)
                .source(SYSTEM)
                .build();

        userRepository.save(user);
    }

    public Store seedStore() {

        User user = User
                .builder()
                .email("store@example.com")
                .password("$782HH$3&")
                .role(STORE)
                .source(SYSTEM)
                .build();

        User savedUser = userRepository.save(user);

        Store store = Store
                .builder()
                .storeId(0L)
                .name("Fitness Life")
                .domain("store.example.com")
                .city("New York City")
                .industry("Food")
                .phoneNumber("+1 787-32801")
                .state("New York")
                .lifeCycleStage("0000")
                .user(savedUser)
                .build();

        return storeRepository.save(store);
    }

    public Customer seedCustomer(Store store) {

        User user = User
                .builder()
                .email("customer@gmail.com")
                .password("&*3dh3S")
                .role(CUSTOMER)
                .source(SYSTEM)
                .build();

        User savedUser = userRepository.save(user);

        Customer customer = Customer
                .builder()
                .customerId(0L)
                .name("Austin")
                .lastName("Smith")
                .phoneNumber("+1 909-3321245")
                .user(savedUser)
                .store(store)
                .build();

        return customerRepository.save(customer);
    }

    public Contact seedContact(Store store) {

        Contact contact = Contact
                .builder()
                .contactId(0L)
                .type(SUPPLIER)
                .firstname("Samuel")
                .lastname("Ballesteros")
                .company("Daily Dairy Inc.")
                .email("savidoficial09@gmail.com")
                .website("www.daily.dairy.com")
                .phone("+57 322 5447725")
                .lifecyclestage("?")
                .store(store)
                .build();

        return contactRepository.save(contact);
    }

    public Product seedProduct(Store store, Contact contact) {

        Product product = Product
                .builder()
                .productId(0L)
                .name("Almond Milk")
                .description("The most delicious milk")
                .price(2000.00)
                .hsSkU("0000-0000-0000-0000")
                .hsCostOfGoodsSold("1000000")
                .hsRecurringBillingPeriod("3")
                .contact(contact)
                .store(store)
                .build();

        return productRepository.save(product);
    }

    public void seedStock(Product product) {

        Stock stock = Stock
                .builder()
                .quantity(12)
                .refillQuantity(100)
                .notificationTriggeringQuantity(11)
                .product(product)
                .build();

        stockRepository.save(stock);
    }
}