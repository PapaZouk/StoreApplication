package pl.zajavka.business;

import pl.zajavka.domain.Opinion;

import java.util.List;

public interface OpinionRepository {
    Opinion create(Opinion opinion);

    Opinion find(String email);
    void removeAll();

    int removeAll(String email);

    int removeAll(int minStars, int maxStars);

    List<Opinion> findAll(int minStars, int maxStars);
}
