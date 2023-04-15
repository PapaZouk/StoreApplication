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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

    // TODO
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
    @DisplayName("Should create new producer successfully")
    void thisProducerShouldBeCreatedSuccessfully() {
        // given
        final Producer producer = producerService.create(StoreFixtures.someProducer());

        // when
        Producer result = producerService.find(producer.getId());

        // then
        assertNotNull(result);
        assertEquals(producer, result);
    }

    // TODO
    @Test
    @DisplayName("Should create new product successfully")
    void thisProductShouldBeCreatedSuccessfully() {
        // given
        Producer producer = producerService.create(StoreFixtures.someProducer());
        Product product = productService.create(StoreFixtures.someProduct(producer));

        // when
        Product result = productService.find(product.getProductCode());

        // then
        assertNotNull(result);
        assertEquals(product.getProductCode(), result.getProductCode());
    }

    @Test
    @DisplayName("Should create new purchase successfully")
    void thisPurchaseShouldBeCreatedSuccessfully() {
        // given
        final Customer customer = customerService.create(StoreFixtures.someCustomer());
        final Producer producer = producerService.create(StoreFixtures.someProducer());
        final Product product = productService.create(StoreFixtures.someProduct(producer));
        final Purchase purchase = purchaseService.create(StoreFixtures.somePurchase(customer, product));

//        // when
//        Purchase result = purchaseService.find(purchase.getCustomerId().getEmail());
//
//        // then
//        assertNotNull(result);
    }

    @Test
    @DisplayName("Should create new Opinion in database successfully")
    void shouldCreateNewOpinionInDataBase() {
        // given
        Producer producer = producerService.create(StoreFixtures.someProducer());
        Product product1 = productService.create(StoreFixtures.someProduct(producer).withProductCode("xpAx"));
        Customer customer = customerService.create(StoreFixtures.someCustomer());
        Opinion opinion = opinionService.create(StoreFixtures.someOpinion(customer, product1));

        // when
        Opinion result = opinionService.find(opinion.getCustomerId().getEmail());
//
//        // then
        assertNotNull(result);
        assertEquals(opinion.getStars(), result.getStars());
        assertEquals(opinion.getComment(), result.getComment());
    }
}