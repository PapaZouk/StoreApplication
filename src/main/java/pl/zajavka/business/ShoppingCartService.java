package pl.zajavka.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.zajavka.domain.Customer;
import pl.zajavka.domain.Product;
import pl.zajavka.domain.Purchase;

import java.time.OffsetDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class ShoppingCartService {

    private final CustomerService customerService;
    private final ProductService productService;
    private final PurchaseService purchaseService;

    public void makePurchase(String email, String productCode, int quantity) {
        Customer customer = customerService.find(email);
        Product product = productService.find(productCode);
        Purchase purchase = purchaseService.create(Purchase.builder()
                .customerId(customer)
                .productId(product)
                .quantity(quantity)
                .dateTime(OffsetDateTime.now())
                .build());

        log.info("Customer: [{}] made a purchase for product: [{}], quantity: [{}]",
                customer.getEmail(),
                product.getProductCode(),
                quantity);

        log.debug("Customer: [{}] made a purchase for product: [{}], quantity: [{}], purchase: [{}]",
                customer.getEmail(),
                product.getProductCode(),
                quantity,
                purchase);
    }
}
