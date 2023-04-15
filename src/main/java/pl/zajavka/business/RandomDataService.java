package pl.zajavka.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.zajavka.domain.*;
import pl.zajavka.infrastructure.database.*;

@Slf4j
@Service
@AllArgsConstructor
public class RandomDataService {

    private final RandomDataPreparationService randomDataPreparationService;
    private final CustomerDatabaseRepository customerRepository;
    private final ProducerDatabaseRepository producerRepository;
    private final ProductDatabaseRepository productRepository;
    private final OpinionDatabaseRepository opinionRepository;
    private final PurchaseDatabaseRepository purchaseRepository;

    public void create () {
        Customer customer = customerRepository.create(randomDataPreparationService.createCustomer());
        Producer producer = producerRepository.create(randomDataPreparationService.createProducer());
        Product product = productRepository.create(randomDataPreparationService.createProduct(producer));
        Opinion opinion = opinionRepository.create(randomDataPreparationService.createOpinion(customer, product));
        Purchase purchase = purchaseRepository.create(randomDataPreparationService.createPurchase(customer, product));

        log.debug("Customer created: [{}]", customer);
        log.debug("Producer created: [{}]", producer);
        log.debug("Product created: [{}]", product);
        log.debug("Opinion created: [{}]", opinion);
        log.debug("Purchase created: [{}]", purchase);
    }
}
