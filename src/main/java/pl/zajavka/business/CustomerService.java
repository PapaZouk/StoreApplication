package pl.zajavka.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.domain.Customer;
import pl.zajavka.infrastructure.database.CustomerDatabaseRepository;

@Slf4j
@Service
@AllArgsConstructor
public class CustomerService {

    private OpinionService opinionService;
    private PurchaseService purchaseService;
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
}
