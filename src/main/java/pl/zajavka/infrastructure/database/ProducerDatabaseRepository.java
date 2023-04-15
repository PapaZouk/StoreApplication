package pl.zajavka.infrastructure.database;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Repository;
import pl.zajavka.business.ProducerRepository;
import pl.zajavka.domain.Producer;

import java.util.Map;
import java.util.Optional;

import static pl.zajavka.infrastructure.configuration.DatabaseConfiguration.*;

@Repository
@AllArgsConstructor
public class ProducerDatabaseRepository implements ProducerRepository {

    private static final String DELETE_FROM_PRODUCER = "DELETE FROM producer WHERE 1 = 1";
    private final SimpleDriverDataSource simpleDriverDataSource;
    private final DatabaseMapper databaseMapper;

    @Override
    public Producer create(Producer producer) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(simpleDriverDataSource)
                .withTableName(PRODUCER_TABLE)
                .usingGeneratedKeyColumns(PRODUCER_TABLE_PKEY);
        Map<String, ?> params = databaseMapper.mapProducer(producer);
        Number producerId = jdbcInsert.executeAndReturnKey(params);
        return producer.withId((long) producerId.intValue());
    }

    @Override
    public void removeAll() {
        new JdbcTemplate(simpleDriverDataSource).update(DELETE_FROM_PRODUCER);
    }

    @Override
    public Optional<Producer> find(Long id) {
        final var jdbcTemplate = new NamedParameterJdbcTemplate(simpleDriverDataSource);
        var params = Map.of("id", id);
        return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_ONE_PRODUCER_WHERE_ID, params, databaseMapper::mapProducer));
    }

    @Override
    public Optional<Producer> find(String producerName) {
        final var jdbcTemplate = new NamedParameterJdbcTemplate(simpleDriverDataSource);
        var params = Map.of("producer_name", producerName);
        return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_ONE_PRODUCER_WHERE_NAME, params, databaseMapper::mapProducer));
    }

}
