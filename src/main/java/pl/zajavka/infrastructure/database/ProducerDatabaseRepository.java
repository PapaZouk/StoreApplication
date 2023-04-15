package pl.zajavka.infrastructure.database;

import org.springframework.stereotype.Repository;
import pl.zajavka.business.ProducerRepository;
import pl.zajavka.domain.Producer;

@Repository
public class ProducerDatabaseRepository implements ProducerRepository {
    @Override
    public Producer create(Producer producer) {
        return null;
    }
}
