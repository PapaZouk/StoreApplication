package pl.zajavka.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

@Slf4j
@Service
@AllArgsConstructor
public class ReloadDataService {

    private CustomerService customerService;
    private ProducerService producerService;
    private ProductService productService;
    private OpinionService opinionService;
    private PurchaseService purchaseService;
    private RandomDataService randomDataService;

    private ReloadDataRepository reloadDataRepository;

    @Transactional
    public void loadRandomData(int num) {
        removeAllDatabaseData();
        for (int i = 0; i < num; i++) {
            randomDataService.create();
        }
        log.info("Successfully loaded 20 random data");
    }

    public void removeAllDatabaseData() {
        opinionService.removeAll();
        purchaseService.removeAll();
        productService.removeAll();
        producerService.removeAll();
        customerService.removeAll();
    }

    @Transactional
    public void reloadData() {
        removeAllDatabaseData();

        try {
            Path filePath = ResourceUtils.getFile("classpath:w15-project-sql-inserts.sql").toPath();

            Stream.of(Files.readString(filePath).split("INSERT"))
                    .filter(line -> !line.isBlank())
                    .map(line -> "INSERT" + line)
                    .toList()
                    .forEach(sql -> reloadDataRepository.run(sql));
            log.info("Reloading data finished successfully!");
        } catch (Exception e) {
            log.error("Unable to load SQL inserts", e);
        }
    }
}
