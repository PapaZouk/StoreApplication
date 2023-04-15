package pl.zajavka.business;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.domain.Producer;
import pl.zajavka.infrastructure.database.ProducerDatabaseRepository;

@Service
@AllArgsConstructor
public class ProducerService {

    private ProducerDatabaseRepository producerDatabaseRepository;
    private ProductService productService;

    @Transactional
    public Producer create(Producer producer) {
        return producerDatabaseRepository.create(producer);
    }

    public void removeAll() {
        producerDatabaseRepository.removeAll();
        productService.removeAll();
    }

    @Transactional
    public Producer find(Long id) {
        return producerDatabaseRepository.find(id)
                .orElseThrow(() -> new RuntimeException("Producer with ID: [%s] is missing".formatted(id)));
    }

    @Transactional
    public Producer find(String producerName) {
        return producerDatabaseRepository.find(producerName)
                .orElseThrow(() -> new RuntimeException("Producer with name: [%s] is missing".formatted(producerName)));
    }
}
