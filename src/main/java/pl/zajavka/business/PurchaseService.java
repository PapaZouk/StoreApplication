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

    public void removeAll() {
        purchaseDatabaseRepository.removeAll();
    }

    @Transactional
    public Purchase create(Purchase purchase) {
        return purchaseDatabaseRepository.create(purchase);
    }

    public Purchase find(String email) {
        return purchaseDatabaseRepository.find(email)
                .orElseThrow(() -> new RuntimeException("Purchase from user with email: [%s] is missing".formatted(email)));
    }

    @Transactional
    public int remove(String email) {
        return purchaseDatabaseRepository.remove(email);
    }

    @Transactional
    public int removeAll(String email) {
        return purchaseDatabaseRepository.removeAll(email);
    }

    public List<Purchase> findAll(String email, String productCode) {
        return purchaseDatabaseRepository.findAll(email, productCode);
    }

    public List<Purchase> findAll() {
        return purchaseDatabaseRepository.findAll();
    }

    public List<Purchase> findAllByProductCode(String productCode) {
        return purchaseDatabaseRepository.findAll(productCode);
    }

    public void removeAllByProductCode(String productCode) {
        purchaseDatabaseRepository.removeAllForPurchasesWithProductCode(productCode);
    }
}
