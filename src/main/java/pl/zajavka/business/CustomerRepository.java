package pl.zajavka.business;

import pl.zajavka.domain.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {
    Customer create(Customer customer);

    Optional<Customer> find(String email);

    void removeAll();

    List<Customer> findAll();

    int remove(String email);
}
