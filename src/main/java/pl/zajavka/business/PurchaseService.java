package pl.zajavka.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.domain.Purchase;
import pl.zajavka.infrastructure.database.PurchaseDatabaseRepository;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class PurchaseService {

    private PurchaseDatabaseRepository purchaseDatabaseRepository;

    @Transactional
    public Purchase create(Purchase purchase) {
        return purchaseDatabaseRepository.create(purchase);
    }

    public Purchase find(String email) {
        return purchaseDatabaseRepository.find(email)
                .orElseThrow(() -> new RuntimeException("Purchase from user with email: [%s] is missing".formatted(email)));
    }

    public List<Purchase> findAll() {
        return purchaseDatabaseRepository.findAll();
    }

    public List<Purchase> findAll(final String email, final String productCode) {
        return purchaseDatabaseRepository.findAll(email, productCode);
    }

    public List<Purchase> findAllByProductCode(final String productCode) {
        return purchaseDatabaseRepository.findAll(productCode);
    }

    @Transactional
    public void remove(String email) {
        purchaseDatabaseRepository.removeAll(email);
    }

    public void removeAll() {
        purchaseDatabaseRepository.removeAll();
    }

    @Transactional
    public int removeAll(String email) {
        return purchaseDatabaseRepository.removeAll(email);
    }

    public void removeAllByProductCode(String productCode) {
        purchaseDatabaseRepository.removeAllForPurchasesWithProductCode(productCode);
    }
}
