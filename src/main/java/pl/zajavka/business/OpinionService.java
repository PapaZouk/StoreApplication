package pl.zajavka.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.domain.Opinion;
import pl.zajavka.infrastructure.database.OpinionDatabaseRepository;

@Slf4j
@Service
@AllArgsConstructor
public class OpinionService {

    private OpinionDatabaseRepository opinionDatabaseRepository;
    public void removeAll() {
      opinionDatabaseRepository.removeAll();
    }

    @Transactional
    public Opinion create(Opinion opinion) {
        return opinionDatabaseRepository.create(opinion);
    }

    public Opinion find(String email) {
        return opinionDatabaseRepository.find(email);
    }
}
