package pl.zajavka.domain;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Component
public class StoreFixtures {
    public static Producer someProducer() {
        return Producer.builder()
                .producerName("someProducerName")
                .address("someAddress")
                .build();
    }

    public static Customer someCustomer() {
        return Customer.builder()
                .userName("someUserName")
                .email("someExampleEmail@example.com")
                .name("someName")
                .surname("someSurname")
                .dateOfBirth(LocalDate.of(1990, 11, 11))
                .telephoneNumber("+09983218502")
                .build();
    }

    public static Product someProduct(Producer producer) {
        return Product.builder()
                .productCode("LoCCXacXk")
                .productName("someProductName")
                .productPrice(BigDecimal.valueOf(92.99))
                .adultsOnly(false)
                .description("someDescription")
                .producerId(producer)
                .build();
    }

    public static Purchase somePurchase(Customer customer, Product product1) {
        return Purchase.builder()
                .customerId(customer)
                .productId(product1)
                .quantity(5)
                .dateTime(OffsetDateTime.of(2010, 11, 11, 20, 20, 0, 0,
                        ZoneOffset.ofHours(4)))
                .build();
    }

    public static Opinion someOpinion(Customer customer, Product product1) {
        return Opinion.builder()
                .customerId(customer)
                .productId(product1)
                .stars((byte) 5)
                .comment("someComment")
                .dateTime(OffsetDateTime.of(2010, 11, 11, 21, 25, 0, 0,
                        ZoneOffset.ofHours(4)))
                .build();
    }
}
