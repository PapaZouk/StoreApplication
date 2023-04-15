package pl.zajavka.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void loadRandomData() {
        removeAllDatabaseData();
        for (int i = 0; i < 20; i++) {
            randomDataService.create();
        }
    }

    public void removeAllDatabaseData() {
        opinionService.removeAll();
        purchaseService.removeAll();
        productService.removeAll();
        producerService.removeAll();
        customerService.removeAll();
    }
}
