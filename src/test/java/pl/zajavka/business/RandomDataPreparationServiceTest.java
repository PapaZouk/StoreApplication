package pl.zajavka.business;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import pl.zajavka.domain.Customer;
import pl.zajavka.infrastructure.configuration.ApplicationConfiguration;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringJUnitConfig(classes = ApplicationConfiguration.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class RandomDataPreparationServiceTest {

    private RandomDataPreparationService dataPreparationService;

    @Test
    @DisplayName("Should create random user successfully")
    void testCreateCustomer() {
        // given
        // when
        Customer customer = dataPreparationService.createCustomer();
        // then
        assertNotNull(customer);
    }

    @Test
    void createProducer() {
    }

    @Test
    void createProduct() {
    }

    @Test
    void createOpinion() {
    }

    @Test
    void createPurchase() {
    }
}