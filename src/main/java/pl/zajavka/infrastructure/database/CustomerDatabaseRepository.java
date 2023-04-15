package pl.zajavka.infrastructure.database;

import org.springframework.stereotype.Repository;
import pl.zajavka.business.CustomerRepository;
import pl.zajavka.domain.Customer;

@Repository
public class CustomerDatabaseRepository implements CustomerRepository {
    @Override
    public Customer create(Customer customer) {
        return null;
    }
}
