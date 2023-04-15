package pl.zajavka.business;

import pl.zajavka.domain.Purchase;

public interface PurchaseRepository {
    Purchase create(Purchase purchase);
}
