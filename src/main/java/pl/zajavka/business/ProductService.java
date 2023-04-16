package pl.zajavka.business;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.domain.Product;
import pl.zajavka.infrastructure.database.ProductDatabaseRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {

    private ProductDatabaseRepository productDatabaseRepository;

    @Transactional
    public Product create(Product product) {
        return productDatabaseRepository.create(product);
    }

    public void removeAll() {
        productDatabaseRepository.removeAll();
    }

    public Product find(String productCode) {
        return productDatabaseRepository.find(productCode)
                .orElseThrow(() -> new RuntimeException("Product with code: [%s] is missing".formatted(productCode)));
    }

    public List<Product> findAll() {
        return productDatabaseRepository.findAll();
    }
}
