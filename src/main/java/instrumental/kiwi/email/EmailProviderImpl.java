package instrumental.kiwi.email;

import instrumental.kiwi.contact.model.Contact;
import instrumental.kiwi.customer.model.Customer;
import instrumental.kiwi.order.model.Order;
import instrumental.kiwi.product.model.Product;
import instrumental.kiwi.security.user.model.User;
import instrumental.kiwi.stock.model.Stock;
import instrumental.kiwi.store.model.Store;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDate;

import static com.fasterxml.jackson.core.JsonEncoding.UTF8;
import static instrumental.kiwi.email.EmailContextVariable.*;
import static instrumental.kiwi.email.EmailSubject.*;
import static instrumental.kiwi.email.EmailTemplatePath.*;

@Async
@Component
@RequiredArgsConstructor
public class EmailProviderImpl implements EmailProvider {

    @Value("${spring.mail.username}")
    private String fromEmail;

    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;

    @Override
    public void sendOrderBillEmail(Order order) {

        try {

            Customer customer = order.getCustomer();
            User user = customer.getUser();
            String customerEmail = user.getEmail();

            Store store = customer.getStore();
            String storeName = store.getName();
            String storeCity = store.getCity();
            String storeState = store.getState();
            String storePhoneNumber = store.getPhoneNumber();
            Double totalPrice = order.getTotalPrice();

            Context context = new Context();

            context.setVariable(DATE, LocalDate.now());
            context.setVariable(STORE_NAME, storeName);
            context.setVariable(STORE_CITY, storeCity);
            context.setVariable(STORE_STATE, storeState);
            context.setVariable(TOTAL_PRICE, totalPrice);
            context.setVariable(STORE_PHONE_NUMBER, storePhoneNumber);

            String text = templateEngine.process(ORDER_BILL, context);

            MimeMessage message = getMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(
                    message, true, UTF8.getJavaName());

            helper.setPriority(1);
            helper.setFrom(fromEmail);
            helper.setTo(customerEmail);
            helper.setText(text, true);
            helper.setSubject(ORDER_BILL_SUBJECT);

            emailSender.send(message);

        } catch(Exception e) {
            throw new RuntimeException("Couldn't send email");
        }
    }

    @Override
    public void sendStockRequestEmail(Stock stock) {

        try {

            Integer refillQuantity = stock.getRefillQuantity();

            Product product = stock.getProduct();

            String productName = product.getName();
            String productSku = product.getHsSkU();

            Store store = product.getStore();
            String storeName = store.getName();
            String storePhoneNumber = store.getPhoneNumber();
            String storeCity = store.getCity();
            String storeState = store.getState();

            Context context = new Context();

            Contact supplier = product.getContact();
            String supplierEmail = supplier.getEmail();

            context.setVariable(DATE, LocalDate.now());
            context.setVariable(STORE_NAME, storeName);
            context.setVariable(STORE_CITY, storeCity);
            context.setVariable(PRODUCT_SKU, productSku);
            context.setVariable(STORE_STATE, storeState);
            context.setVariable(PRODUCT_NAME, productName);
            context.setVariable(STORE_PHONE_NUMBER, storePhoneNumber);
            context.setVariable(STOCK_REFILL_QUANTITY, refillQuantity);

            String text = templateEngine.process(STOCK_REQUEST, context);

            MimeMessage message = getMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(
                    message, true, UTF8.getJavaName());

            helper.setPriority(1);
            helper.setFrom(fromEmail);
            helper.setTo(supplierEmail);
            helper.setText(text, true);
            helper.setSubject(STOCK_REQUEST_SUBJECT);

            emailSender.send(message);

        } catch(Exception e) {
            throw new RuntimeException(e.getCause() + " " + e.getLocalizedMessage());
        }
    }

    @Override
    public void sendTicketClaimEmail(Product product, Integer quantity) {

        try {

            String productName = product.getName();
            String productSku = product.getHsSkU();

            Store store = product.getStore();
            String storeName = store.getName();
            String storePhoneNumber = store.getPhoneNumber();
            String storeCity = store.getCity();
            String storeState = store.getState();

            Context context = new Context();

            Contact supplier = product.getContact();
            String supplierEmail = supplier.getEmail();

            context.setVariable(DATE, LocalDate.now());
            context.setVariable(STORE_NAME, storeName);
            context.setVariable(STORE_CITY, storeCity);
            context.setVariable(PRODUCT_SKU, productSku);
            context.setVariable(STORE_STATE, storeState);
            context.setVariable(PRODUCT_NAME, productName);
            context.setVariable(STORE_PHONE_NUMBER, storePhoneNumber);

            String text = templateEngine.process(TICKET_CLAIM, context);

            MimeMessage message = getMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(
                    message, true, UTF8.getJavaName());

            helper.setPriority(1);
            helper.setFrom(fromEmail);
            helper.setTo(supplierEmail);
            helper.setText(text, true);
            helper.setSubject(PRODUCT_CHANGE_SUBJECT);

            emailSender.send(message);

        } catch(Exception e) {
            throw new RuntimeException("Couldn't send email");
        }
    }

    private MimeMessage getMimeMessage(){
        return emailSender.createMimeMessage();
    }
}