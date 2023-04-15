package pl.zajavka.business;

import pl.zajavka.domain.Purchase;

import java.util.Optional;

public interface PurchaseRepository {
    Purchase create(Purchase purchase);

    void removeAll();

    Optional<Purchase> find(String email);
}
