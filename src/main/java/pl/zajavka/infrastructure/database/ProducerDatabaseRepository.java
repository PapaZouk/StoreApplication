package pl.zajavka.infrastructure.database;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Repository;
import pl.zajavka.business.ProducerRepository;
import pl.zajavka.domain.Producer;

@Repository
@AllArgsConstructor
public class ProducerDatabaseRepository implements ProducerRepository {

    private static final String DELETE_FROM_PRODUCER = "DELETE FROM producer WHERE 1 = 1";
    private final SimpleDriverDataSource simpleDriverDataSource;

    @Override
    public Producer create(Producer producer) {
        return null;
    }

    @Override
    public void removeAll() {
        new JdbcTemplate(simpleDriverDataSource).update(DELETE_FROM_PRODUCER);
    }
}
