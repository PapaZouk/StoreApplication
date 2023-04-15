package pl.zajavka.infrastructure.database;

import org.springframework.stereotype.Repository;
import pl.zajavka.business.ProductRepository;
import pl.zajavka.domain.Product;

@Repository
public class ProductDatabaseRepository implements ProductRepository {
    @Override
    public Product create(Product product) {
        return null;
    }
}
