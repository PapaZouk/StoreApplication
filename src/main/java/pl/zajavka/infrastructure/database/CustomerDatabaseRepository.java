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
    private final SimpleDriverDataSource simpleDriverDataSource;
    private DatabaseMapper databaseMapper;

    @Override
    public Customer create(Customer customer) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(simpleDriverDataSource)
                .withTableName(CUSTOMER_TABLE)
                .usingGeneratedKeyColumns(CUSTOMER_TABLE_PKEY);

        Number customerId = jdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(customer));
        return customer.withId((long) customerId.intValue());
    }

    @Override
    public int remove(String email) {
        final var jdbcTemplate = new NamedParameterJdbcTemplate(simpleDriverDataSource);
        var params = Map.of("email", email);
        int removedResult = jdbcTemplate.update(DELETE_FROM_CUSTOMER_WHERE_EMAIL, params);
        log.info("Rows removed: [{}]", removedResult);
        return removedResult;
    }

    @Override
    public Optional<Customer> find(String email) {
        final var jdbcTemplate = new NamedParameterJdbcTemplate(simpleDriverDataSource);
        var params = Map.of("email", email);
        return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_ONE_USER_WHERE_EMAIL, params, databaseMapper::mapCustomer));
    }

    @Override
    public Optional<Customer> find(long id) {
        final var jdbcTemplate = new NamedParameterJdbcTemplate(simpleDriverDataSource);
        var params = Map.of("id", id);
        Customer customer = jdbcTemplate.queryForObject(SELECT_ONE_CUSTOMER_WHERE_ID,
                params,
                databaseMapper::mapCustomer);
        log.info("Found customer: [{}]", customer);
        return Optional.ofNullable(customer);
    }

    @Override
    public List<Customer> findAll() {
//        return new JdbcTemplate(simpleDriverDataSource)
//                .query(SELECT_ALL_CUSTOMERS, new BeanPropertyRowMapper<>(Customer.class));
        JdbcTemplate jdbcTemplate = new JdbcTemplate(simpleDriverDataSource);
        return jdbcTemplate.query(SELECT_ALL_CUSTOMERS, (rs, rowNum) -> databaseMapper.mapCustomer(rs, rowNum));
    }

    @Override
    public void removeAll() {
        int removeResult = new JdbcTemplate(simpleDriverDataSource).update(DELETE_FROM_CUSTOMER);
        log.warn("Rows removed: [{}]", removeResult);
    }

}
