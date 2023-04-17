package pl.zajavka.infrastructure.database;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Repository;
import pl.zajavka.business.CustomerRepository;
import pl.zajavka.domain.Customer;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static pl.zajavka.infrastructure.configuration.DatabaseConfiguration.*;

@Slf4j
@Repository
@AllArgsConstructor
public class CustomerDatabaseRepository implements CustomerRepository {
    public static final String SELECT_ALL_CUSTOMERS = "SELECT * FROM customer";
    public static final String DELETE_FROM_CUSTOMER = "DELETE FROM customer WHERE 1 = 1";
    public static final String SELECT_ONE_CUSTOMER_WHERE_ID = "SELECT * FROM customer WHERE id = :id";
    public static final String SELECT_ONE_USER_WHERE_EMAIL = "SELECT * FROM customer WHERE email = :email";
    public static final String DELETE_FROM_CUSTOMER_WHERE_EMAIL = "DELETE FROM customer WHERE email = :email";


    private final SimpleDriverDataSource simpleDriverDataSource;
    private DatabaseMapper databaseMapper;

    @Override
    public Customer create(final Customer customer) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(simpleDriverDataSource)
                .withTableName(CUSTOMER_TABLE)
                .usingGeneratedKeyColumns(CUSTOMER_TABLE_PKEY);

        Number customerId = jdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(customer));
        return customer.withId((long) customerId.intValue());
    }

    @Override
    public int remove(final String email) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(simpleDriverDataSource);

        int removedResult = jdbcTemplate.update(
                DELETE_FROM_CUSTOMER_WHERE_EMAIL,
                Map.of("email", email));

        log.info("Rows removed: [{}]", removedResult);
        return removedResult;
    }

    @Override
    public Optional<Customer> find(final String email) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(simpleDriverDataSource);

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    SELECT_ONE_USER_WHERE_EMAIL,
                    Map.of("email", email),
                    databaseMapper::mapCustomer));
        } catch (Exception e) {
            log.warn("Trying to find non-existing customer with email: [{}]", email);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Customer> find(final long id) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(simpleDriverDataSource);

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    SELECT_ONE_CUSTOMER_WHERE_ID,
                    Map.of("id", id),
                    databaseMapper::mapCustomer));
        } catch (Exception e) {
            log.warn("Trying to find non-existing customer with id: [{}]", id);
            return Optional.empty();
        }
    }

    @Override
    public List<Customer> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(simpleDriverDataSource);
        return jdbcTemplate.query(
                SELECT_ALL_CUSTOMERS,
                (rs, rowNum) -> databaseMapper.mapCustomer(rs, rowNum));
    }

    @Override
    public void removeAll() {
        int removeResult = new JdbcTemplate(simpleDriverDataSource).update(DELETE_FROM_CUSTOMER);
        log.warn("Rows removed: [{}]", removeResult);
    }

}
