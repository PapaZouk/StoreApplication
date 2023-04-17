package pl.zajavka.infrastructure.database;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import pl.zajavka.business.*;
import pl.zajavka.domain.Opinion;
import pl.zajavka.domain.Product;
import pl.zajavka.domain.Purchase;
import pl.zajavka.infrastructure.configuration.ApplicationConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringJUnitConfig(classes = ApplicationConfiguration.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ProductServiceTest {

    private ReloadDataService reloadDataService;
    private ProductService productService;
    private OpinionService opinionService;
    private PurchaseService purchaseService;

    @BeforeEach
    void setUp() {
        assertNotNull(reloadDataService);
        assertNotNull(productService);
        assertNotNull(opinionService);
        assertNotNull(purchaseService);

        reloadDataService.reloadData();
    }

    @Test
    @DisplayName("That product trace should be removed successfully")
    void thatProductTraceShouldBeRemovedSuccessfully() {
        // given
        final var productCode = "68084-618";
        productService.find(productCode);

        List<Opinion> opinionsBefore = opinionService.findAllByProductCode(productCode);
        List<Purchase> purchasesBefore = purchaseService.findAllByProductCode(productCode);

        assertEquals(3, opinionsBefore.size());
        assertEquals(4, purchasesBefore.size());

        // when
        productService.removeAllWithProductCode(productCode);

        // then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.find(productCode));
        assertEquals("Product with code: [%s] is missing".formatted(productCode), exception.getMessage());

        assertThrows(RuntimeException.class, () -> productService.find(productCode));

        assertTrue(opinionService.findAllByProductCode(productCode).isEmpty());
        assertTrue(purchaseService.findAllByProductCode(productCode).isEmpty());
    }
}
