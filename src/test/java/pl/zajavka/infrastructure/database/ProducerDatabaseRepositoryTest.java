package pl.zajavka.infrastructure.database;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import pl.zajavka.business.ProducerService;
import pl.zajavka.business.ReloadDataService;
import pl.zajavka.domain.Producer;
import pl.zajavka.domain.StoreFixtures;
import pl.zajavka.infrastructure.configuration.ApplicationConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringJUnitConfig(classes = ApplicationConfiguration.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ProducerDatabaseRepositoryTest {

    private ReloadDataService reloadDataService;
    private ProducerService producerService;

    @BeforeEach
    void setUp() {
        assertNotNull(reloadDataService);
        assertNotNull(producerService);

        reloadDataService.loadRandomData();
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

    @Test
    @DisplayName("Should create new Producer in database successfully")
    void shouldCreateNewProducerInDatabase() {
        // given
        Producer producer = producerService.create(StoreFixtures.someProducer());

        // when
        Producer result = producerService.find(producer.getProducerName());

        // then
        assertNotNull(result);
        assertEquals(producer.getProducerName(), result.getProducerName());
        assertEquals(producer.getAddress(), result.getAddress());
    }
}
