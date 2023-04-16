package pl.zajavka.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.domain.Customer;
import pl.zajavka.infrastructure.database.CustomerDatabaseRepository;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class CustomerService {

    public static final String CUSTOMER_AGE_WITH_EMAIL_S_IS_OVER_40 = "Could not remove purchase because customer age with email: [%s] is over 40";
    private OpinionService opinionService;
    private PurchaseService purchaseService;
    private ProducerService producerService;
    private CustomerDatabaseRepository customerDatabaseRepository;

    private static boolean isOlderThan40(Customer customer) {
        return LocalDate.now().getYear() - customer.getDateOfBirth().getYear() > 40;
    }

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

    public Customer find(long id) {
        return customerDatabaseRepository.find(id)
                .orElseThrow(() -> new RuntimeException("Customer with id: [%d] is missing".formatted(id)));
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

        if (isOlderThan40(customer)) {
            throw new RuntimeException(CUSTOMER_AGE_WITH_EMAIL_S_IS_OVER_40.formatted(email));
        }

        return customerDatabaseRepository.remove(email);
    }

    public int removeWithStars(int minStars, int maxStars) {
        var customers = opinionService.findAll(1, 3).stream()
                .map(opinion -> opinion.getCustomerId().getId())
                .distinct()
                .toList();

        int removedCustomers = 0;
        for (Long id : customers) {
            String email = find(id).getEmail();
            try {
                removedCustomers += remove(email);
            } catch (RuntimeException e) {
                log.error("Error while removing customer with email: [{}], reason: [{}]",
                        email, e.getMessage());
            }
        }

        log.info("Removed customers: [{}] with stars between [{}] and [{}]", removedCustomers, minStars, maxStars);
        return removedCustomers;
    }
}
