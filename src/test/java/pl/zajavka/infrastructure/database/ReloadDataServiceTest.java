package pl.zajavka.infrastructure.database;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
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
public class ReloadDataServiceTest {

    private ReloadDataService reloadDataService;
    private CustomerService customerService;
    private OpinionService opinionService;
    private ProducerService producerService;
    private ProductService productService;
    private PurchaseService purchaseService;

    @BeforeEach
    void setUp() {
        assertNotNull(reloadDataService);
    }

    @Test
    void thatDataIsReloaded() {
        // given, when
        reloadDataService.reloadData();

        // then
        List<Customer> allCustomers = customerService.findAll();
        List<Opinion> allOpinions = opinionService.findAll();
        List<Producer> allProducers = producerService.findAll();
        List<Product> allProducts = productService.findAll();
        List<Purchase> allPurchases = purchaseService.findAll();

        // then
        assertEquals(100, allCustomers.size());
        assertEquals(140, allOpinions.size());
        assertEquals(20, allProducers.size());
        assertEquals(50, allProducts.size());
        assertEquals(300, allPurchases.size());
    }

}
