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
    private RandomDataService randomDataService;

    @Transactional
    public void loadRandomData() {
        producerService.removeAll();
        productService.removeAll();
        customerService.removeAll();
        for (int i = 0; i < 20; i++) {
            randomDataService.create();
        }
    }
}
