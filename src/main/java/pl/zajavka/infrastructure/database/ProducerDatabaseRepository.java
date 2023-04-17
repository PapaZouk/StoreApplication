package pl.zajavka.infrastructure.database;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Repository;
import pl.zajavka.business.ProducerRepository;
import pl.zajavka.domain.Producer;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static pl.zajavka.infrastructure.configuration.DatabaseConfiguration.PRODUCER_TABLE;
import static pl.zajavka.infrastructure.configuration.DatabaseConfiguration.PRODUCER_TABLE_PKEY;

@Slf4j
@Repository
@AllArgsConstructor
public class ProducerDatabaseRepository implements ProducerRepository {

    public static final String SELECT_ALL_PRODUCER = "SELECT * FROM producer";
    private static final String DELETE_FROM_PRODUCER = "DELETE FROM producer WHERE 1 = 1";
    public static final String SELECT_ONE_PRODUCER_WHERE_ID = "SELECT * FROM producer WHERE id = :id";
    public static final String SELECT_ONE_PRODUCER_WHERE_NAME = "SELECT * FROM producer WHERE producer_name = :producer_name";
    private final SimpleDriverDataSource simpleDriverDataSource;
    private final DatabaseMapper databaseMapper;

    @Override
    public Producer create(final Producer producer) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(simpleDriverDataSource)
                .withTableName(PRODUCER_TABLE)
                .usingGeneratedKeyColumns(PRODUCER_TABLE_PKEY);

        Number producerId = jdbcInsert.executeAndReturnKey(databaseMapper.mapProducer(producer));
        return producer.withId((long) producerId.intValue());
    }

    @Override
    public Optional<Producer> find(final Long id) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(simpleDriverDataSource);

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    SELECT_ONE_PRODUCER_WHERE_ID,
                    Map.of("id", id),
                    databaseMapper::mapProducer));
        } catch (Exception e) {
            log.warn("Trying to find non-existing producer with id: [{}]", id);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Producer> find(final String producerName) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(simpleDriverDataSource);

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    SELECT_ONE_PRODUCER_WHERE_NAME,
                    Map.of("producer_name", producerName),
                    databaseMapper::mapProducer));
        } catch (Exception e) {
            log.warn("Trying to find non-existing producer with name: [{}]", producerName);
            return Optional.empty();
        }
    }

    @Override
    public List<Producer> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(simpleDriverDataSource);
        return jdbcTemplate.query(
                SELECT_ALL_PRODUCER,
                (rs, rowNum) -> databaseMapper.mapProducer(rs, rowNum));
    }

    @Override
    public void removeAll() {
        new JdbcTemplate(simpleDriverDataSource).update(DELETE_FROM_PRODUCER);
    }

}
