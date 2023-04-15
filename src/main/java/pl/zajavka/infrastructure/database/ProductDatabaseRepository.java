package pl.zajavka.infrastructure.database;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Repository;
import pl.zajavka.business.ProductRepository;
import pl.zajavka.domain.Product;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static pl.zajavka.infrastructure.configuration.DatabaseConfiguration.PRODUCT_TABLE;
import static pl.zajavka.infrastructure.configuration.DatabaseConfiguration.PRODUCT_TABLE_PKEY;

@Repository
@AllArgsConstructor
public class ProductDatabaseRepository implements ProductRepository {

    private static final String DELETE_FROM_PRODUCT = "DELETE FROM product WHERE 1 = 1";
    private static final String SELECT_WHERE_PRODUCT_CODE = "SELECT * FROM product WHERE product_code = :product_code";

    public static final String INSERT_PRODUCT = "INSERT INTO product (product_code, product_name, product_price, adults_only, " +
            "description, producer_id) VALUES (:productCode, :productName, :productPrice, :adultsOnly, :description, :producerId)";
    private SimpleDriverDataSource simpleDriverDataSource;
    private DatabaseMapper databaseMapper;

    @Override
    public Product create(Product product) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(simpleDriverDataSource)
                .withTableName(PRODUCT_TABLE)
                .usingGeneratedKeyColumns(PRODUCT_TABLE_PKEY);

//        Map<String, ?> params = databaseMapper.mapProduct(product);
        Map<String, ?> params = databaseMapper.mapProduct(product);
        Number productId = jdbcInsert.executeAndReturnKey(params);
        return product.withId((long) productId.intValue());
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
        return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_WHERE_PRODUCT_CODE, params, (rs, rowNum) -> databaseMapper.mapProduct(rs, rowNum)));
    }
}
