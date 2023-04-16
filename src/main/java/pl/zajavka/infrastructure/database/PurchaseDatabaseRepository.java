package pl.zajavka.infrastructure.database;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Repository;
import pl.zajavka.business.PurchaseRepository;
import pl.zajavka.domain.Customer;
import pl.zajavka.domain.Purchase;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static pl.zajavka.infrastructure.configuration.DatabaseConfiguration.*;

@Slf4j
@Repository
@AllArgsConstructor
public class PurchaseDatabaseRepository implements PurchaseRepository {

    public static final String SELECT_ALL_PURCHASE = "SELECT * FROM purchase";
    public static final String CUSTOMER_WITH_EMAIL_S_IS_TOO_OLD =
            "Could not remove purchase because customer with email: [%s] is too old";
    private static final String DELETE_FROM_PURCHASE = "DELETE FROM purchase WHERE 1 = 1";
    private static final String SELECT_PURCHASES_WHERE_EMAIL = "SELECT * FROM purchase WHERE customer_id IN " +
            "(SELECT customer_id FROM customer WHERE email = :email)";
    private static final String REMOVE_PURCHASE_WHERE_EMAIL = "DELETE FROM purchase WHERE email IN " +
            "(SELECT email FROM customer WHERE email = :email)";
    private static final String SELECT_ALL_WHERE_CUSTOMER_EMAIL_AND_PRODUCT_CODE = """
            SELECT * FROM purchase AS pur
                INNER JOIN customer AS cus ON cus.id = pur.customer_id
                INNER JOIN product AS prod ON cus.id = prod.product_id
                WHERE cus.email = :email AND prod.product_code = :productCode
                ORDER BY date_time
            """;
    private SimpleDriverDataSource simpleDriverDataSource;
    private DatabaseMapper databaseMapper;

    @Override
    public Purchase create(Purchase purchase) {
        final var jdbcInsert = new SimpleJdbcInsert(simpleDriverDataSource)
                .withTableName(PURCHASE_TABLE)
                .usingGeneratedKeyColumns(PURCHASE_TABLE_PKEY);

        Map<String, ?> params = databaseMapper.mapPurchase(purchase);
        Number purchaseId = jdbcInsert.executeAndReturnKey(params);
        return purchase.withId((long) purchaseId.intValue());
    }

    @Override
    public void removeAll() {
        new JdbcTemplate(simpleDriverDataSource).update(DELETE_FROM_PURCHASE);
    }

    @Override
    public Optional<Purchase> find(String email) {
        final var jdbcTemplate = new NamedParameterJdbcTemplate(simpleDriverDataSource);
        var params = Map.of("email", email);
        return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_PURCHASES_WHERE_EMAIL, params, (rs, rowNum) -> databaseMapper.mapPurchase(rs, rowNum)));
    }

    @Override
    public List<Purchase> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(simpleDriverDataSource);
        return jdbcTemplate.query(SELECT_ALL_PURCHASE, (rs, rowNum) -> databaseMapper.mapPurchase(rs, rowNum));
    }

    @Override
    public int remove(String email) {
        final var jdbcTemplate = new NamedParameterJdbcTemplate(simpleDriverDataSource);
        var params = Map.of("email", email);
        Customer customer = jdbcTemplate.queryForObject(SELECT_ONE_USER_WHERE_EMAIL, params, databaseMapper::mapCustomer);

        assert customer != null;
        if (LocalDate.now().getYear() - customer.getDateOfBirth().getYear() > 40) {
            throw new RuntimeException(CUSTOMER_WITH_EMAIL_S_IS_TOO_OLD.formatted(email));
        }
        int removedRows = jdbcTemplate.update(REMOVE_PURCHASE_WHERE_EMAIL, params);
        log.info("Successfully removed: [{}] rows", removedRows);
        return removedRows;
    }

    @Override
    public int removeAll(String email) {
        final var jdbcTemplate = new NamedParameterJdbcTemplate(simpleDriverDataSource);
        var params = Map.of("email", email);
        int removedPurchases = jdbcTemplate.update(DELETE_FROM_PURCHASE_WHERE_EMAIL, params);
        log.info("Removed purchase rows for customer with email: [{}]", removedPurchases);
        return removedPurchases;
    }

    @Override
    public List<Purchase> findAll(String email, String productCode) {
        final var jdbcTemplate = new NamedParameterJdbcTemplate(simpleDriverDataSource);
        return jdbcTemplate.query(
                SELECT_ALL_WHERE_CUSTOMER_EMAIL_AND_PRODUCT_CODE,
                Map.of("email", email, "product_code", productCode),
                (rs, rowNum) -> databaseMapper.mapPurchase(rs, rowNum));
    }
}
