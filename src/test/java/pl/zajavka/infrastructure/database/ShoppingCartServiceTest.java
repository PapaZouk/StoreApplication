package pl.zajavka.infrastructure.database;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import pl.zajavka.business.*;
import pl.zajavka.domain.Customer;
import pl.zajavka.domain.Producer;
import pl.zajavka.domain.Product;
import pl.zajavka.domain.StoreFixtures;
import pl.zajavka.infrastructure.configuration.ApplicationConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringJUnitConfig(classes = ApplicationConfiguration.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ShoppingCartServiceTest {

    private ReloadDataService reloadDataService;
    private CustomerService customerService;
    private ProducerService producerService;
    private ProductService productService;
    private PurchaseService purchaseService;
    private ShoppingCartService shoppingCartService;

    @BeforeEach
    void setUp() {
        assertNotNull(reloadDataService);
        assertNotNull(customerService);
        assertNotNull(producerService);
        assertNotNull(productService);
        assertNotNull(purchaseService);
        assertNotNull(shoppingCartService);

        reloadDataService.reloadData();
    }

    @Test
    @DisplayName("Product should be bought by customer")
    void thatProductCanBeBoughtByCustomer() {
        // given
        Customer customer = customerService.create(StoreFixtures.someCustomer());
        Producer producer = producerService.create(StoreFixtures.someProducer());
        Product product = productService.create(StoreFixtures.someProduct(producer));
        purchaseService.create(StoreFixtures.somePurchase(customer, product));

        final var before = purchaseService.findAll(customer.getEmail(), product.getProductCode());

        // when
        shoppingCartService.makePurchase(customer.getEmail(), product.getProductCode(), 10);
        final var after = purchaseService.findAll(customer.getEmail(), product.getProductCode());

        // then
        assertEquals(before.size() + 1, after.size());
    }
}
