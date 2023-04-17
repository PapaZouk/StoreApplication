package pl.zajavka.business;

import pl.zajavka.domain.Opinion;

import java.util.List;
import java.util.Optional;

public interface OpinionRepository {
    Opinion create(Opinion opinion);

    Optional<Opinion> find(String email);
    List<Opinion> findAll();

    List<Opinion> findAll(int minStars, int maxStars);

    List<Opinion> findAll(String productCode);

    void removeAll();

    int removeAll(String email);

    int removeAll(int minStars, int maxStars);

    void removeAllForPurchasesWithProductCode(String productCode);
}
