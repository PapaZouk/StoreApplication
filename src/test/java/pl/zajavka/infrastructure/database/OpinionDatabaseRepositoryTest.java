package pl.zajavka.infrastructure.database;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import pl.zajavka.business.*;
import pl.zajavka.domain.*;
import pl.zajavka.infrastructure.configuration.ApplicationConfiguration;

import javax.sound.sampled.Line;

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
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        assertNotNull(reloadDataService);
        assertNotNull(opinionService);
        assertNotNull(producerService);
        assertNotNull(productService);
        assertNotNull(customerService);

        reloadDataService.removeAllDatabaseData();
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

    @Test
    @DisplayName("Should remove opinion from database successfully")
    void thatOpinionWillBeRemovedFromDatabase() {
        // given
        Customer customer = customerService.create(StoreFixtures.someCustomer());
        Producer producer = producerService.create(StoreFixtures.someProducer());
        Product product = productService.create(StoreFixtures.someProduct(producer));
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
        reloadDataService.loadRandomData();

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
}
