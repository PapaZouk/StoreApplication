package pl.zajavka.business;

import pl.zajavka.domain.Purchase;

import java.util.List;
import java.util.Optional;

public interface PurchaseRepository {
    Purchase create(Purchase purchase);

    void removeAll();

    Optional<Purchase> find(String email);

    int remove(String email);

    int removeAll(String email);

    List<Purchase> findAll(String email, String productCode);

    List<Purchase> findAll();

    List<Purchase> findAll(String productCode);

    void removeAllForPurchasesWithProductCode(String productCode);
}
