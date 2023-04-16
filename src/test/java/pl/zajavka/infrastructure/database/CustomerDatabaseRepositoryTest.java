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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringJUnitConfig(classes = ApplicationConfiguration.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class CustomerDatabaseRepositoryTest {

    private ReloadDataService reloadDataService;
    private CustomerService customerService;
    private OpinionService opinionService;
    private PurchaseService purchaseService;
    private ProductService productService;
    private ProducerService producerService;

    @BeforeEach
    void setUp() {
        assertNotNull(reloadDataService);
        assertNotNull(customerService);
        assertNotNull(opinionService);
        assertNotNull(purchaseService);
        assertNotNull(productService);
        assertNotNull(producerService);
        customerService.removeAll();
        opinionService.removeAll();
        productService.removeAll();
        producerService.removeAll();
        purchaseService.removeAll();

    }

    @Test
    @DisplayName("Should reload data too database successfully")
    void dataShouldBeLoadedToDatabaseSuccessfully() {
        // given
        int expectedListSize = 20;

        // when
        reloadDataService.loadRandomData();
        List<Customer> result = customerService.findAll();

        // then
        assertNotNull(result);
        assertEquals(expectedListSize, result.size());
    }

    @Test
    @DisplayName("Should return a list of 20 customers successfully")
    void shouldReturnListOfCustomers() {
        // given
        int expectedSize = 3;
        Customer customer1 = customerService.create(StoreFixtures
                .someCustomer()
                .withUserName("someUserName1")
                .withEmail("someEmail1@example.com"));
        Customer customer2 = customerService.create(StoreFixtures
                .someCustomer()
                .withUserName("someUserName2")
                .withEmail("someEmail2@example.com"));
        Customer customer3 = customerService.create(StoreFixtures
                .someCustomer()
                .withUserName("someUserName3")
                .withEmail("someEmail3@example.com"));

        List<Customer> result = customerService.findAll();
        for (Customer customer : result) {
            log.info("Found customer: [{}]", customer);
        }

        // then
        assertNotNull(result);
        assertEquals(expectedSize, result.size());
    }

    @Test
    @DisplayName("Should create new customer successfully")
    void thisCustomerShouldBeCreatedSuccessfully() {
        // given, when
        final Customer customer = customerService.create(StoreFixtures.someCustomer());

        // when
        Customer result = customerService.find(customer.getEmail());
        log.info("Found customer: [{}]", result);

        // then
        assertNotNull(result);
        assertEquals(customer, result);
    }

    @Test
    @DisplayName("Customer should be removed successfully")
    void testRemoveCustomer() {
        // given
        Customer expected = customerService.create(StoreFixtures.someCustomer());
        Producer producer = producerService.create(StoreFixtures.someProducer());
        Product product = productService.create(StoreFixtures.someProduct(producer));
        Opinion opinion = opinionService.create(StoreFixtures.someOpinion(expected, product));
        Purchase purchase = purchaseService.create(StoreFixtures.somePurchase(expected, product));

        Customer actual = customerService.find(expected.getEmail());


        log.info("Actual customer created: [{}]", actual);
        log.info("Opinion created: [{}]", opinion);

        // when
        int removedCustomer = customerService.remove(expected.getEmail());
        log.info("Removed customer: [{}]", removedCustomer);

        // then
        assertEquals(1, removedCustomer);
    }

    @Test
    @DisplayName("Transaction should fail when removing customer")
    void thatCustomerWhenRemovingWillFail() {
        // given
        Customer customer = customerService.create(StoreFixtures.someCustomer()
                .withDateOfBirth(LocalDate.of(1950, 2, 12)));
        Producer producer = producerService.create(StoreFixtures.someProducer());
        Product product = productService.create(StoreFixtures.someProduct(producer));
        Purchase purchase = purchaseService.create(StoreFixtures.somePurchase(customer, product));
        Opinion opinion = opinionService.create(StoreFixtures.someOpinion(customer, product));

        log.info("Customer: [{}]", customer);
        log.info("Producer: [{}]", producer);
        log.info("Product: [{}]", product);
        log.info("Purchase: [{}]", purchase);
        log.info("Opinion: [{}]", opinion);

        // when
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> customerService.remove(customer.getEmail()));

        // then
        assertEquals(
                "Could not remove purchase because customer with email: [%s] is too old".formatted(customer.getEmail()),
                exception.getMessage()
        );

    }
}