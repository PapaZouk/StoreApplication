package pl.zajavka.business;

import pl.zajavka.domain.Opinion;

public interface OpinionRepository {
    Opinion create(Opinion opinion);

    Opinion find(String email);
    void removeAll();

    int removeAll(String email);
}
