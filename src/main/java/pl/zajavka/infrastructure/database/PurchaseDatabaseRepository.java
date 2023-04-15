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
import pl.zajavka.business.PurchaseRepository;
import pl.zajavka.domain.Purchase;

import java.util.Map;
import java.util.Optional;

import static pl.zajavka.infrastructure.configuration.DatabaseConfiguration.PURCHASE_TABLE;
import static pl.zajavka.infrastructure.configuration.DatabaseConfiguration.PURCHASE_TABLE_PKEY;

@Repository
@AllArgsConstructor
public class PurchaseDatabaseRepository implements PurchaseRepository {

    private static final String DELETE_FROM_PURCHASE = "DELETE FROM purchase WHERE 1 = 1";
    private static final String SELECT_PURCHASES_WHERE_EMAIL = "SELECT * FROM purchase WHERE email IN " +
            "(SELECT C.email FROM customer C WHERE email = :email)";
    private SimpleDriverDataSource simpleDriverDataSource;
    private DatabaseMapper databaseMapper;

    @Override
    public Purchase create(Purchase purchase) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(simpleDriverDataSource)
                .withTableName(PURCHASE_TABLE)
                .usingGeneratedKeyColumns(PURCHASE_TABLE_PKEY);

//        Map<String, ?> params = databaseMapper.mapPurchase(purchase);
//        Number purchaseId = jdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(params));
        Number purchaseId = jdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(purchase));
        return purchase.withId((long) purchaseId.intValue());
    }

    @Override
    public void removeAll() {
        new JdbcTemplate(simpleDriverDataSource).update(DELETE_FROM_PURCHASE);
    }

    @Override
    public Optional<Purchase> find(String email) {
        final var jdbcTemplate = new NamedParameterJdbcTemplate(simpleDriverDataSource);
        var params = Map.of("email", email);
        RowMapper<Purchase> purchaseRowMapper = (rs, rowNum) -> databaseMapper.mapPurchase(rs, rowNum);
        return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_PURCHASES_WHERE_EMAIL, params, purchaseRowMapper));
    }
}
