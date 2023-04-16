package pl.zajavka.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.domain.Customer;
import pl.zajavka.domain.Opinion;
import pl.zajavka.infrastructure.database.CustomerDatabaseRepository;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class CustomerService {

    private OpinionService opinionService;
    private PurchaseService purchaseService;
    private ProducerService producerService;
    private CustomerDatabaseRepository customerDatabaseRepository;

    @Transactional
    public Customer create(Customer customer) {
        return customerDatabaseRepository.create(customer);
    }

    @Transactional
    public void removeAll() {
        opinionService.removeAll();
        purchaseService.removeAll();
        customerDatabaseRepository.removeAll();
    }

    public Customer find(String email) {
        return customerDatabaseRepository.find(email)
                .orElseThrow(() -> new RuntimeException("Customer with email: [%s] is missing".formatted(email)));
    }

    public List<Customer> findAll() {
        return customerDatabaseRepository.findAll();
    }

    @Transactional
    public int remove(String email) {
        Customer customer = find(email);

        int removeResult1 = opinionService.removeAll(customer.getEmail());
        log.info("Successfully removed opinions: [{}]", removeResult1);

        int removeResult2 = purchaseService.removeAll(customer.getEmail());
        log.info("Successfully removed purchases: [{}]", removeResult2);

        if (LocalDate.now().getYear() - customer.getDateOfBirth().getYear() > 40) {
            throw new RuntimeException(
                    "Could not remove purchase because customer with email: [%s] is too old".formatted(email));
        }

        return customerDatabaseRepository.remove(email);
    }
}
