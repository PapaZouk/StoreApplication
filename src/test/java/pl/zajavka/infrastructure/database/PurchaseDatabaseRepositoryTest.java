package pl.zajavka.infrastructure.database;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import pl.zajavka.business.*;
import pl.zajavka.domain.*;
import pl.zajavka.infrastructure.configuration.ApplicationConfiguration;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringJUnitConfig(classes = ApplicationConfiguration.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PurchaseDatabaseRepositoryTest {
    private ReloadDataService reloadDataService;
    private PurchaseService purchaseService;
    private CustomerService customerService;
    private ProducerService producerService;
    private ProductService productService;
    private OpinionService opinionService;

    @BeforeEach
    void setUp() {
        assertNotNull(reloadDataService);
        assertNotNull(purchaseService);
        assertNotNull(customerService);
        assertNotNull(producerService);
        assertNotNull(productService);
        assertNotNull(opinionService);

        reloadDataService.removeAllDatabaseData();
    }

    @Test
    @DisplayName("Should create new purchase successfully")
    void thisPurchaseShouldBeCreatedSuccessfully() {
        // given
        final Customer customer = customerService.create(StoreFixtures.someCustomer());
        final Producer producer = producerService.create(StoreFixtures.someProducer());
        final Product product = productService.create(StoreFixtures.someProduct(producer));
        final Purchase purchase = purchaseService.create(StoreFixtures.somePurchase(customer, product));

        // when
        Purchase result = purchaseService.find(purchase.getCustomerId().getEmail());

        // then
        assertNotNull(result);
        assertEquals(purchase.getQuantity(), result.getQuantity());
        assertEquals(purchase.getDateTime(), result.getDateTime());
    }

    @Test
    @DisplayName("Should NOT remove purchase when customer is older than 40")
    void thatPurchaseIsNotRemovedWhenCustomerRemovingFails() {
        // given
        final var customer = customerService.create(StoreFixtures
                .someCustomer()
                .withDateOfBirth(LocalDate.of(1950, 5, 20)));
        final var producer = producerService.create(StoreFixtures.someProducer());
        final var product1 = productService.create(StoreFixtures.someProduct(producer));
        final var purchase = purchaseService.create(StoreFixtures.somePurchase(customer, product1));
        final var opinion = opinionService.create(StoreFixtures.someOpinion(customer, product1));
        purchaseService.create(StoreFixtures.somePurchase(customer, product1).withQuantity(1));
        purchaseService.create(StoreFixtures.somePurchase(customer, product1).withQuantity(3));

        assertEquals(customer, customerService.find(customer.getEmail()));

        // when
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> purchaseService.remove(customer.getEmail()));

        // then
        assertEquals(
                "Could not remove purchase because customer with email: [%s] is too old".formatted(customer.getEmail()),
                exception.getMessage()
        );
    }
}
