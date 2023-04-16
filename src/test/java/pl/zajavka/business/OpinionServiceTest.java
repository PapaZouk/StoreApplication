package pl.zajavka.business;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zajavka.domain.Opinion;
import pl.zajavka.domain.Purchase;
import pl.zajavka.domain.StoreFixtures;
import pl.zajavka.infrastructure.database.OpinionDatabaseRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class OpinionServiceTest {

    @InjectMocks
    private OpinionService opinionService;

    @Mock
    private OpinionDatabaseRepository opinionDatabaseRepository;

    @Mock
    private PurchaseService purchaseService;

    @Test
    @DisplayName("Opinion will be created for product that customer bought")
    void thatOpinionCanBeCreatedForProductThatCustomerAlreadyBought() {
        // given
        final var customer = StoreFixtures.someCustomer();
        final var producer = StoreFixtures.someProducer();
        final var product = StoreFixtures.someProduct(producer);
        final var purchase = StoreFixtures.somePurchase(customer, product);
        final var opinion = StoreFixtures.someOpinion(customer, product);

        when(purchaseService.findAll(customer.getEmail(), product.getProductCode()))
                .thenReturn(List.of(purchase.withId(1L)));

        when(opinionDatabaseRepository.create(opinion)).thenReturn(opinion.withId(1L));

        // when
        Opinion result = opinionService.create(opinion);

        // then
        verify(opinionDatabaseRepository).create(opinion);
        assertEquals(opinion.withId(1L), result);
    }

    @Test
    @DisplayName("Opinion will NOT be created for product that customer bought")
    void thatOpinionCanNotBeCreatedForProductThatCustomerAlreadyBought() {
        // given
        final var customer = StoreFixtures.someCustomer();
        final var producer = StoreFixtures.someProducer();
        final var product = StoreFixtures.someProduct(producer);
        final var opinion = StoreFixtures.someOpinion(customer, product);

        when(purchaseService.findAll(customer.getEmail(), product.getProductCode())).thenReturn(List.of());

        // when
        Throwable exception = assertThrows(RuntimeException.class, () -> opinionService.create(opinion));
        assertEquals(
                "Customer: [%s] wants to give opinion for product: [%s] but there is no purchase"
                .formatted(customer.getEmail(), product.getProductCode()),
                exception.getMessage());

        verify(opinionDatabaseRepository, Mockito.never()).create(any(Opinion.class));
    }
}