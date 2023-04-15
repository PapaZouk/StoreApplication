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

//        reloadDataService.loadRandomData();

    }

    @Test
    @DisplayName("Should create new customer successfully")
    void thisCustomerShouldBeCreatedSuccessfully() {
        // given, when
        final Customer customer = customerService.create(StoreFixtures.someCustomer());
        final Producer producer = producerService.create(StoreFixtures.someProducer());
        final Product product1 = productService.create(StoreFixtures.someProduct(producer));
        final Purchase purchase = purchaseService.create(StoreFixtures.somePurchase(customer, product1));
        final Opinion opinion = opinionService.create(StoreFixtures.someOpinion(customer, product1));

        // when
        Customer result = customerService.find(customer.getEmail());
        log.info("Found customer: [{}]", result);

        // then
        assertNotNull(result);
        assertEquals(customer, result);
    }
}