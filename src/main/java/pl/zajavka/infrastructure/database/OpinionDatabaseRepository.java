package pl.zajavka.infrastructure.database;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Repository;
import pl.zajavka.business.CustomerService;
import pl.zajavka.business.OpinionRepository;
import pl.zajavka.domain.Customer;
import pl.zajavka.domain.Opinion;
import pl.zajavka.infrastructure.configuration.DatabaseConfiguration;

import java.util.Map;

import static pl.zajavka.infrastructure.configuration.DatabaseConfiguration.*;

@Slf4j
@Repository
@AllArgsConstructor
public class OpinionDatabaseRepository implements OpinionRepository {

    private static final String DELETE_FROM_OPINION = "DELETE FROM opinion WHERE 1 = 1";
    private static final String SELECT_ONE_OPINION_WHERE_EMAIL = "SELECT * FROM opinion WHERE customer_id IN " +
            "(SELECT customer_id FROM customer WHERE email = :email)";
    private final SimpleDriverDataSource simpleDriverDataSource;
    private final DatabaseMapper databaseMapper;

    @Override
    public Opinion create(Opinion opinion) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(simpleDriverDataSource)
                .withTableName(OPINION_TABLE)
                .usingGeneratedKeyColumns(OPINION_TABLE_PKEY);

        Map<String, ?> params = databaseMapper.mapOpinion(opinion);
        Number opinionId = jdbcInsert.executeAndReturnKey(params);
        return opinion.withId((long) opinionId.intValue());
    }

    @Override
    public Opinion find(String email) {
        final var jdbcTemplate = new NamedParameterJdbcTemplate(simpleDriverDataSource);

        var params = Map.of("email", email);
        return jdbcTemplate.queryForObject(SELECT_ONE_OPINION_WHERE_EMAIL, params, databaseMapper::mapOpinion);
    }

    @Override
    public void removeAll() {
        new JdbcTemplate(simpleDriverDataSource).update(DELETE_FROM_OPINION);
    }

    @Override
    public void removeAll(String email) {
        new JdbcTemplate(simpleDriverDataSource).update(DELETE_FROM_OPINION_WHERE_EMAIL, email);
    }
}
