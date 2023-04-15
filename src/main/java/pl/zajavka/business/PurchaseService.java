package pl.zajavka.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.domain.Purchase;
import pl.zajavka.infrastructure.database.PurchaseDatabaseRepository;

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
    public void remove(String email) {
        purchaseDatabaseRepository.remove(email);
    }


    public void removeAll(String email) {
        purchaseDatabaseRepository.removeAll(email);
    }
}
