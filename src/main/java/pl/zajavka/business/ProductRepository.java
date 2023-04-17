package pl.zajavka.business;

import pl.zajavka.domain.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product create(Product product);

    void removeAllByProductCode();

    Optional<Product> find(String productCode);

    List<Product> findAll();

    void removeAllByProductCode(String productCode);
}
