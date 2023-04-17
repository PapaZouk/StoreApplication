package pl.zajavka.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.domain.Opinion;
import pl.zajavka.domain.Purchase;
import pl.zajavka.infrastructure.database.OpinionDatabaseRepository;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class OpinionService {

    private final PurchaseService purchaseService;

    private final OpinionDatabaseRepository opinionDatabaseRepository;

    public void removeAll() {
        opinionDatabaseRepository.removeAll();
    }

    @Transactional
    public Opinion create(Opinion opinion) {
        String email = opinion.getCustomerId().getEmail();
        String productCode = opinion.getProductId().getProductCode();

        List<Purchase> purchases = purchaseService.findAll(email, productCode);

        log.debug("Customer: [{}] made: [{}] purchases for product: [{}]", email, purchases.size(), productCode);

        if (purchases.isEmpty()) {
            throw new RuntimeException("Customer: [%s] wants to give opinion for product: [%s] but there is no purchase"
                            .formatted(email, productCode));
        }
        return opinionDatabaseRepository.create(opinion);
    }

    public Opinion find(String email) {
        return opinionDatabaseRepository.find(email);
    }

    public int removeAll(String email) {
        return opinionDatabaseRepository.removeAll(email);
    }

    public int removeAll(int minStars, int maxStars) {
        return opinionDatabaseRepository.removeAll(minStars, maxStars);
    }

    public List<Opinion> findAll(int minStars, int maxStars) {
        return opinionDatabaseRepository.findAll(minStars, maxStars);
    }

    public List<Opinion> findAll() {
        return opinionDatabaseRepository.findAll();
    }

    public List<Opinion> findAllByProductCode(String productCode) {
        return opinionDatabaseRepository.findAll(productCode);
    }

    public void removeAllByProductCode(String productCode) {
        opinionDatabaseRepository.removeAllForPurchasesWithProductCode(productCode);
    }
}
