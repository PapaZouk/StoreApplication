package pl.zajavka.business;

import pl.zajavka.domain.Producer;

import java.util.List;
import java.util.Optional;

public interface ProducerRepository {
    Producer create(Producer producer);

    Optional<Producer> find(Long id);

    Optional<Producer> find(String producerName);

    List<Producer> findAll();

    void removeAll();
}
