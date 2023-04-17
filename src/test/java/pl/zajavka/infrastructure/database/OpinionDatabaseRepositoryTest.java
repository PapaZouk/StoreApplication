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

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringJUnitConfig(classes = ApplicationConfiguration.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class OpinionDatabaseRepositoryTest {

    private ReloadDataService reloadDataService;
    private OpinionService opinionService;
    private ProducerService producerService;
    private ProductService productService;
    private PurchaseService purchaseService;
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        assertNotNull(reloadDataService);
        assertNotNull(opinionService);
        assertNotNull(producerService);
        assertNotNull(productService);
        assertNotNull(productService);
        assertNotNull(customerService);

        reloadDataService.reloadData();
    }

    @Test
    @DisplayName("Should create new Opinion in database successfully")
    void shouldCreateNewOpinionInDataBase() {
        // given
        Producer producer = producerService.create(StoreFixtures.someProducer());
        Product product1 = productService.create(StoreFixtures.someProduct(producer).withProductCode("xpAx"));
        Customer customer = customerService.create(StoreFixtures.someCustomer());
        Purchase purchase = purchaseService.create(StoreFixtures.somePurchase(customer, product1));
        Opinion opinion = opinionService.create(StoreFixtures.someOpinion(customer, product1));

        // when
        Opinion result = opinionService.find(opinion.getCustomerId().getEmail());
//
//        // then
        assertNotNull(result);
        assertEquals(opinion.getStars(), result.getStars());
        assertEquals(opinion.getComment(), result.getComment());
    }

    @Test
    @DisplayName("Should remove opinion from database successfully")
    void thatOpinionWillBeRemovedFromDatabase() {
        // given
        Customer customer = customerService.create(StoreFixtures.someCustomer());
        Producer producer = producerService.create(StoreFixtures.someProducer());
        Product product = productService.create(StoreFixtures.someProduct(producer));
        Purchase purchase = purchaseService.create(StoreFixtures.somePurchase(customer, product));
        Opinion opinion = opinionService.create(StoreFixtures.someOpinion(customer, product));

        // when
        int result = opinionService.removeAll(customer.getEmail());
        log.info("Successfully removed opinions: [{}]", result);

        // then
        assertEquals(1, result);
    }

    @Test
    @DisplayName("Should remove opinion where stars are lower than 4")
    void theseOpinionsWillBeRemovedIfStarsLowerThan4() {
        // given
        reloadDataService.removeAllDatabaseData();
        reloadDataService.reloadData();

        int resultFound = opinionService.findAll(1, 4).size();

        // when
        int resultDeleted = opinionService.removeAll(1, 4);
        log.info("Successfully removed opinions: [{}]", resultDeleted);

        // then
        assertEquals(resultFound, resultDeleted);
    }

    @Test
    @DisplayName("Should find all opinions with stars between 1 and 3")
    void shouldFindAllOpinionsWithStarsBetween1And3() {
        // given
        reloadDataService.removeAllDatabaseData();

        Customer customer1 = customerService.create(StoreFixtures.someCustomer());
        Customer customer2 = customerService.create(StoreFixtures.someCustomer()
                .withName("Romek")
                .withUserName("romekUser")
                .withEmail("romek@example.com"));

        Producer producer = producerService.create(StoreFixtures.someProducer());

        Product product1 = productService.create(StoreFixtures.someProduct(producer));
        Product product2 = productService.create(StoreFixtures.someProduct(producer)
                .withProductCode("xPfx"));

        Purchase purchase1 = purchaseService.create(StoreFixtures.somePurchase(customer1, product1));
        Purchase purchase2 = purchaseService.create(StoreFixtures.somePurchase(customer2, product2));

        Opinion opinion1 = opinionService.create(
                StoreFixtures.someOpinion(customer1, product1)
                        .withStars((byte) 1)
        );
        Opinion opinion2 = opinionService.create(
                StoreFixtures.someOpinion(customer1, product1)
                        .withStars((byte) 3)
        );


        // when
        List<Opinion> result = opinionService.findAll(1, 3);

        // then
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Should fail finding opinion for provided email")
    void thatFindOpinionWillFail() {
        // given
        String someEmail = "someEmail@example.xyz";

        // when
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> opinionService.find(someEmail)
        );

        // then
        assertEquals("Opinion for email: [%s] doesn't exists".formatted(someEmail), exception.getMessage());
    }
}
