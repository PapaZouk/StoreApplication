package pl.zajavka.infrastructure.database;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Repository;
import pl.zajavka.business.CustomerRepository;
import pl.zajavka.domain.Customer;
import pl.zajavka.infrastructure.configuration.DatabaseConfiguration;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static pl.zajavka.infrastructure.configuration.DatabaseConfiguration.*;

@Slf4j
@Repository
@AllArgsConstructor
public class CustomerDatabaseRepository implements CustomerRepository {

    private final SimpleDriverDataSource simpleDriverDataSource;
    private DatabaseMapper databaseMapper;

    @Override
    public Customer create(Customer customer) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(simpleDriverDataSource)
                .withTableName(CUSTOMER_TABLE)
                .usingGeneratedKeyColumns(CUSTOMER_TABLE_PKEY);

        Number customerId = jdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(customer));
        return customer.withId(customerId.longValue());
    }

    @Override
    public Optional<Customer> find(String email) {
        final var jdbcTemplate = new NamedParameterJdbcTemplate(simpleDriverDataSource);
        var params = Map.of("email", email);
        return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_ONE_USER_WHERE_EMAIL, params, databaseMapper::mapCustomer));
    }

    @Override
    public void removeAll() {
        int removeResult = new JdbcTemplate(simpleDriverDataSource).update(DELETE_FROM_CUSTOMER);
        log.warn("Rows removed: [{}]", removeResult);
    }

    @Override
    public List<Customer> findAll() {
        return new JdbcTemplate(simpleDriverDataSource)
                .query(DatabaseConfiguration.SELECT_ALL_CUSTOMERS, new BeanPropertyRowMapper<>(Customer.class));
    }
}
