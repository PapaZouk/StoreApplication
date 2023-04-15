package pl.zajavka.infrastructure.database;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Repository;
import pl.zajavka.business.ProductRepository;
import pl.zajavka.domain.Product;

@Repository
@AllArgsConstructor
public class ProductDatabaseRepository implements ProductRepository {

    private static final String DELETE_FROM_PRODUCT = "DELETE FROM product WHERE 1 = 1";
    private SimpleDriverDataSource simpleDriverDataSource;

    @Override
    public Product create(Product product) {
        return null;
    }

    @Override
    public void removeAll() {
        new JdbcTemplate(simpleDriverDataSource).update(DELETE_FROM_PRODUCT);
    }
}
