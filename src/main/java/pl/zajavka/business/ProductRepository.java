package pl.zajavka.business;

import pl.zajavka.domain.Product;

import java.util.Optional;

public interface ProductRepository {
    Product create(Product product);

    void removeAll();

    Optional<Product> find(String productCode);
}
