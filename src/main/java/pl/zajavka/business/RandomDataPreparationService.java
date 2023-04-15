package pl.zajavka.business;

import org.springframework.stereotype.Service;
import pl.zajavka.domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Random;
import java.util.stream.IntStream;

@Service
public class RandomDataPreparationService {
    public Customer createCustomer() {
        String name = randomString(65, 90, 1)
                + randomString(97, 122, 10)
                + randomString(48, 57, 2);
        return Customer.builder()
                .userName(name + "User")
                .email(name + "@example.com")
                .name(name)
                .surname("surname")
                .dateOfBirth(LocalDate.of(1990, 11, 11))
                .telephoneNumber("+" + randomString(48,57, 11))
                .build();
    }

    public Producer createProducer() {
        return Producer.builder()
                .producerName(randomString(69, 90, 1) + randomString(97, 122, 10))
                .address("someAddress")
                .build();
    }

    public Product createProduct(Producer producer) {
        return Product.builder()
                .productCode(randomString(65, 90, 3)
                                + randomString(97, 122, 4)
                                + randomString( 48, 57, 2))
                .productName("productName")
                .productPrice(BigDecimal.valueOf(92.99))
                .adultsOnly(new Random().nextBoolean())
                .description("someDescription")
                .producerId(producer)
                .build();

    }

    public Opinion createOpinion(Customer customer, Product product) {
        return Opinion.builder()
                .customerId(customer)
                .productId(product)
                .stars((byte) new Random().nextInt(1, 6))
                .comment("some comment")
                .dateTime(OffsetDateTime.of(2010, 8, 12, 15, 43, 13, 4,
                        ZoneOffset.ofHours(4)))
                .build();
    }

    public Purchase createPurchase(Customer customer, Product product) {
        return Purchase.builder()
                .customerId(customer)
                .productId(product)
                .quantity(new Random().nextInt(1, 50))
                .dateTime(OffsetDateTime.of(2010, 8, 12, 14, 55, 39, 4,
                        ZoneOffset.ofHours(4)))
                .build();

    }

    private int randomInt(int min, int max) {
        return new Random().nextInt(max - min) + min;
    }

    private String randomString(int min, int max, int length) {
        return IntStream.range(0, length)
                .boxed()
                .reduce("", (previous, next) -> previous + (char) randomInt(min, max), String::concat);
    }
}
