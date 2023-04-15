package pl.zajavka.business;

import pl.zajavka.domain.Customer;

public interface CustomerRepository {
    Customer create(Customer customer);
}
