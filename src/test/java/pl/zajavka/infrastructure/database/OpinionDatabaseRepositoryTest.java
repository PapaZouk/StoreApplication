package pl.zajavka.infrastructure.database;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
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
}
