package pl.zajavka.infrastructure.database;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Repository;
import pl.zajavka.business.ProductRepository;
import pl.zajavka.domain.Product;

import java.util.Map;
import java.util.Optional;

import static pl.zajavka.infrastructure.configuration.DatabaseConfiguration.*;

@Repository
@AllArgsConstructor
public class ProductDatabaseRepository implements ProductRepository {

    private static final String DELETE_FROM_PRODUCT = "DELETE FROM product WHERE 1 = 1";
    private static final String SELECT_WHERE_PRODUCT_CODE = "SELECT * FROM product WHERE product_code = :product_code";
    private SimpleDriverDataSource simpleDriverDataSource;
    private DatabaseMapper databaseMapper;

    @Override
    public Product create(Product product) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(simpleDriverDataSource)
                .withTableName(PRODUCT_TABLE)
                .usingGeneratedKeyColumns(PRODUCT_TABLE_PKEY);

        Map<String, ?> params = databaseMapper.mapProduct(product);
//        Number productId = jdbcInsert.executeAndReturnKey(params);
        Number productId = jdbcInsert.executeAndReturnKey(params);
        return product.withId(productId.longValue());
    }

    @Override
    public void removeAll() {
        new JdbcTemplate(simpleDriverDataSource).update(DELETE_FROM_PRODUCT);
    }

    @Override
    public Optional<Product> find(String productCode) {
        final var jdbcTemplate = new NamedParameterJdbcTemplate(simpleDriverDataSource);
        var params = Map.of("product_code", productCode);
        RowMapper<Product> productRowMapper = (rs, rowNum) -> databaseMapper.mapProduct(rs, rowNum);
        return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_WHERE_PRODUCT_CODE, params, productRowMapper));
    }
}
