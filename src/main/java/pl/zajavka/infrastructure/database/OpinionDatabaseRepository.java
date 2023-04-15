package pl.zajavka.infrastructure.database;

import org.springframework.stereotype.Repository;
import pl.zajavka.business.OpinionRepository;
import pl.zajavka.domain.Opinion;

@Repository
public class OpinionDatabaseRepository implements OpinionRepository {
    @Override
    public Opinion create(Opinion opinion) {
        return null;
    }
}
