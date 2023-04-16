package pl.zajavka.business;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import pl.zajavka.domain.*;
import pl.zajavka.infrastructure.configuration.ApplicationConfiguration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(classes = ApplicationConfiguration.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class RandomDataPreparationServiceTest {

    private RandomDataPreparationService dataPreparationService;

    @Test
    @DisplayName("Should create random user successfully")
    void testCreateCustomer() {
        // given
        String expectedSurname = "surname";
        // when
        Customer customer = dataPreparationService.createCustomer();
        // then
        assertNotNull(customer);
        assertEquals(expectedSurname, customer.getSurname());
    }

    @Test
    void createProducer() {
        // given
        String expectedAddress = "someAddress";
        // when
        Producer producer = dataPreparationService.createProducer();
        // then
        assertNotNull(producer);
        assertEquals(expectedAddress, producer.getAddress());
    }

    @Test
    void createProduct() {
        // given
        String expectedProductName = "productName";
        BigDecimal expectedPrice = BigDecimal.valueOf(92.99);
        // when
        Product result = dataPreparationService.createProduct(StoreFixtures.someProducer());
        // then
        assertNotNull(result);
        assertEquals(expectedProductName, result.getProductName());
        assertEquals(expectedPrice, result.getProductPrice());
    }

    @Test
    void createOpinion() {
        // given
        String expectedComment = "some comment";
        OffsetDateTime expectedDateTime = OffsetDateTime.of(2010, 8, 12, 15, 43, 13, 4,
                ZoneOffset.ofHours(4));

        // when
        Opinion result = dataPreparationService.createOpinion(
                StoreFixtures.someCustomer(),
                StoreFixtures.someProduct(StoreFixtures.someProducer()));

        // then
        assertNotNull(result);
        assertEquals(expectedComment, result.getComment());
        assertEquals(expectedDateTime, result.getDateTime());
    }

    @Test
    void createPurchase() {
        // given
        OffsetDateTime expectedDateTime = OffsetDateTime.of(2010, 8, 12, 14, 55, 39, 4,
                ZoneOffset.ofHours(4));
        // when
        Purchase result = dataPreparationService.createPurchase(
                StoreFixtures.someCustomer(),
                StoreFixtures.someProduct(StoreFixtures.someProducer()));
        // then
        assertNotNull(result);
        assertTrue(result.getQuantity() >= 1 && result.getQuantity() <= 50);
        assertEquals(expectedDateTime, result.getDateTime());
    }
}