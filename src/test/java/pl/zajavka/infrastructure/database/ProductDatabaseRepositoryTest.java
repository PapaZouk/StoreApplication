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
import pl.zajavka.business.ProductService;
import pl.zajavka.business.ReloadDataService;
import pl.zajavka.domain.Producer;
import pl.zajavka.domain.Product;
import pl.zajavka.domain.StoreFixtures;
import pl.zajavka.infrastructure.configuration.ApplicationConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringJUnitConfig(classes = ApplicationConfiguration.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ProductDatabaseRepositoryTest {

    private ReloadDataService reloadDataService;
    private ProductService productService;
    private ProducerService producerService;

    @BeforeEach
    void setUp() {
        assertNotNull(reloadDataService);
        assertNotNull(productService);
        assertNotNull(producerService);

        reloadDataService.loadRandomData();
    }

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
}
