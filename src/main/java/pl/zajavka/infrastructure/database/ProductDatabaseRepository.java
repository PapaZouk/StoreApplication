package pl.zajavka.infrastructure.database;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Repository;
import pl.zajavka.business.ProductRepository;
import pl.zajavka.domain.Product;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static pl.zajavka.infrastructure.configuration.DatabaseConfiguration.PRODUCT_TABLE;
import static pl.zajavka.infrastructure.configuration.DatabaseConfiguration.PRODUCT_TABLE_PKEY;

@Slf4j
@Repository
@AllArgsConstructor
public class ProductDatabaseRepository implements ProductRepository {

    public static final String SELECT_ALL_PRODUCT = "SELECT * FROM product";
    public static final String INSERT_PRODUCT = "INSERT INTO product (product_code, product_name, product_price, adults_only, " +
            "description, producer_id) VALUES (:productCode, :productName, :productPrice, :adultsOnly, :description, :producerId)";
    private static final String DELETE_FROM_PRODUCT = "DELETE FROM product WHERE 1 = 1";
    private static final String SELECT_WHERE_PRODUCT_CODE = "SELECT * FROM product WHERE product_code = :product_code";
    private static final String DELETE_ALL_PRODUCT_WHERE_PRODUCT_CODE =
            "DELETE FROM product WHERE product_code = :productCode";
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
    public void removeAllByProductCode() {
        new JdbcTemplate(simpleDriverDataSource).update(DELETE_FROM_PRODUCT);
    }

    @Override
    public Optional<Product> find(String productCode) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(simpleDriverDataSource);

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    SELECT_WHERE_PRODUCT_CODE,
                    Map.of("product_code", productCode),
                    databaseMapper::mapProduct));
        } catch (Exception e) {
            log.warn("Trying to find non-existing productCode: [{}]", productCode);
            return Optional.empty();
        }
    }

    @Override
    public List<Product> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(simpleDriverDataSource);
        return jdbcTemplate.query(SELECT_ALL_PRODUCT, (rs, rowNum) -> databaseMapper.mapProduct(rs, rowNum));
    }

    @Override
    public void removeAllByProductCode(String productCode) {
        new NamedParameterJdbcTemplate(simpleDriverDataSource)
                .update(DELETE_ALL_PRODUCT_WHERE_PRODUCT_CODE, Map.of("productCode", productCode));
    }
}
