package pl.zajavka.business;

import pl.zajavka.domain.Opinion;

public interface OpinionRepository {
    Opinion create(Opinion opinion);
}
