package pl.zajavka.infrastructure.database;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Repository;
import pl.zajavka.business.PurchaseRepository;
import pl.zajavka.domain.Purchase;

@Repository
@AllArgsConstructor
public class PurchaseDatabaseRepository implements PurchaseRepository {

    private static final String DELETE_FROM_PURCHASE = "DELETE FROM purchase WHERE 1 = 1";
    private SimpleDriverDataSource simpleDriverDataSource;

    @Override
    public Purchase create(Purchase purchase) {
        return null;
    }

    @Override
    public void removeAll() {
        new JdbcTemplate(simpleDriverDataSource).update(DELETE_FROM_PURCHASE);
    }
}
