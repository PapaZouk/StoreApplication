package pl.zajavka.business;

import pl.zajavka.domain.Purchase;

import java.util.List;
import java.util.Optional;

public interface PurchaseRepository {
    Purchase create(Purchase purchase);

    Optional<Purchase> find(String email);

    List<Purchase> findAll();

    List<Purchase> findAll(String productCode);

    List<Purchase> findAll(String email, String productCode);

    void removeAll();

    int removeAll(String email);

    void removeAllForPurchasesWithProductCode(String productCode);
}
