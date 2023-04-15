package pl.zajavka.infrastructure.database;

import org.springframework.stereotype.Repository;
import pl.zajavka.business.PurchaseRepository;
import pl.zajavka.domain.Purchase;

@Repository
public class PurchaseDatabaseRepository implements PurchaseRepository {
    @Override
    public Purchase create(Purchase purchase) {
        return null;
    }
}
