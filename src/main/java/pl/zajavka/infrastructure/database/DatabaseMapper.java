package pl.zajavka.infrastructure.database;

import org.springframework.stereotype.Component;
import pl.zajavka.domain.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component
public class DatabaseMapper {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssX");

    public Map<String, ?> mapCustomer(Customer customer) {
        return Map.of(
        "userName", customer.getUserName(),
        "email", customer.getEmail(),
        "name", customer.getName(),
        "surname", customer.getSurname(),
        "dateOfBirth", customer.getDateOfBirth(),
        "telephoneNumber", customer.getTelephoneNumber()
        );
    }

    public Map<String, ?> mapPurchase(Purchase purchase) {
        return Map.of(
        "customerId", purchase.getCustomerId().getId(),
        "productId", purchase.getProductId().getId(),
        "quantity", purchase.getQuantity(),
        "dateTime", DATE_TIME_FORMATTER.format(purchase.getDateTime())
        );
    }
    public Map<String, ?> mapOpinion(Opinion opinion) {
        return Map.of(
            "customerId", opinion.getCustomerId().getId(),
            "productId", opinion.getProductId().getId(),
            "stars", opinion.getStars(),
            "comment", opinion.getComment(),
            "dateTime", DATE_TIME_FORMATTER.format(opinion.getDateTime())
        );
    }

    public Map<String, ?> mapProducer(Producer producer) {
        return Map.of(
        "producerName", producer.getProducerName(),
        "address", producer.getAddress()
        );
    }

    public Map<String, ?> mapProduct(Product product) {
        return Map.of(
        "productCode", product.getProductCode(),
        "productName", product.getProductName(),
        "productPrice", product.getProductPrice(),
        "adultsOnly", product.isAdultsOnly(),
        "description", product.getDescription(),
        "producerId", product.getProducerId()
        );
    }

    @SuppressWarnings("unused")
    public Customer mapCustomer(ResultSet resultSet, int rowNum) throws SQLException {
        return Customer.builder()
                .id(resultSet.getLong("id"))
                .userName(resultSet.getString("user_name"))
                .email(resultSet.getString("email"))
                .name(resultSet.getString("name"))
                .surname(resultSet.getString("surname"))
                .dateOfBirth(LocalDate.parse(resultSet.getString("date_of_birth")))
                .telephoneNumber(resultSet.getString("telephone_number"))
                .build();
    }

    @SuppressWarnings("unused")
    public Producer mapProducer(ResultSet resultSet, int rowNum) throws SQLException {
        return Producer.builder()
                .id(resultSet.getLong("id"))
                .producerName(resultSet.getString("producer_name"))
                .address(resultSet.getString("address"))
                .build();
    }

    @SuppressWarnings("unused")
    public Purchase mapPurchase(ResultSet rs, int rowNum) throws SQLException {
        return Purchase.builder()
                .id(rs.getLong("id"))
                .quantity(rs.getInt("quantity"))
                .dateTime(OffsetDateTime.parse(rs.getString("date_time"), DATE_TIME_FORMATTER)
                        .withOffsetSameInstant(ZoneOffset.ofHours(4)))
                .customerId(Customer.builder().id(rs.getLong("customer_id")).build())
                .productId(Product.builder().id(rs.getLong("product_id")).build())
                .build();
    }

    @SuppressWarnings("unused")
    public Product mapProduct(ResultSet rs, int rowNum) throws SQLException {
        return Product.builder()
                .id(rs.getLong("id"))
                .productCode(rs.getString("product_code"))
                .productName(rs.getString("product_name"))
                .productPrice(rs.getBigDecimal("product_price"))
                .adultsOnly(rs.getBoolean("adults_only"))
                .description(rs.getString("description"))
                .build();
    }
}
