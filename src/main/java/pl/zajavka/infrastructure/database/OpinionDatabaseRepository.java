package pl.zajavka.infrastructure.database;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Repository;
import pl.zajavka.business.OpinionRepository;
import pl.zajavka.domain.Opinion;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static pl.zajavka.infrastructure.configuration.DatabaseConfiguration.*;

@Slf4j
@Repository
@AllArgsConstructor
public class OpinionDatabaseRepository implements OpinionRepository {

    public static final String SELECT_ALL_OPINION = "SELECT * FROM opinion";
    private static final String DELETE_FROM_OPINION = "DELETE FROM opinion WHERE 1 = 1";
    private static final String SELECT_ONE_OPINION_WHERE_EMAIL = """
                SELECT * FROM opinion AS op
                    INNER JOIN customer AS cus ON cus.id = op.customer_id
                    WHERE cus.email = :email
                """;
    public static final String DELETE_FROM_OPINION_WHERE_EMAIL = "DELETE FROM opinion WHERE customer_id IN " +
            "(SELECT id FROM customer C WHERE email = :email)";
    private static final String DELETE_FROM_OPINION_WHERE_STARS_BETWEEN =
            "DELETE FROM opinion WHERE stars BETWEEN :min AND :max";
    private static final String SELECT_ALL_OPINION_WHERE_STARS_BETWEEN =
            "SELECT * FROM opinion WHERE stars BETWEEN :min AND :max";
    private static final String SELECT_ALL_OPINION_WHERE_PRODUCT_CODE = """
            SELECT * FROM opinion AS op
                INNER JOIN product AS prod ON prod.id = op.product_id
                WHERE prod.product_code = :productCode
            """;
    private static final String REMOVE_ALL_OPINION_WHERE_PRODUCT_CODE = """
            DELETE FROM opinion AS op WHERE op.product_id IN
                (SELECT op.product_id FROM product AS prod WHERE prod.product_code = :productCode)
            """;

    private final SimpleDriverDataSource simpleDriverDataSource;
    private final DatabaseMapper databaseMapper;

    @Override
    public Opinion create(Opinion opinion) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(simpleDriverDataSource)
                .withTableName(OPINION_TABLE)
                .usingGeneratedKeyColumns(OPINION_TABLE_PKEY);

        Number opinionId = jdbcInsert.executeAndReturnKey(databaseMapper.mapOpinion(opinion));
        return opinion.withId((long) opinionId.intValue());
    }

    @Override
    public Optional<Opinion> find(String email) {
        final var jdbcTemplate = new NamedParameterJdbcTemplate(simpleDriverDataSource);

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    SELECT_ONE_OPINION_WHERE_EMAIL,
                    Map.of("email", email),
                    databaseMapper::mapOpinion));
        } catch (Exception e) {
            log.warn("Trying to find non-existing opinion for email: [{}]", email);
            return Optional.empty();
        }
    }

    @Override
    public List<Opinion> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(simpleDriverDataSource);
        return jdbcTemplate.query(
                SELECT_ALL_OPINION,
                (rs, rowNum) -> databaseMapper.mapOpinion(rs, rowNum));
    }

    @Override
    public void removeAll() {
        new JdbcTemplate(simpleDriverDataSource).update(DELETE_FROM_OPINION);
    }

    @Override
    public List<Opinion> findAll(int minStars, int maxStars) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(simpleDriverDataSource);

        List<Opinion> opinions = jdbcTemplate.query(
                SELECT_ALL_OPINION_WHERE_STARS_BETWEEN,
                Map.of("min", minStars, "max", maxStars),
                (rs, rowNum) -> databaseMapper.mapOpinion(rs, rowNum));

        log.info("Found: [{}] opinions with stars between [{}] and [{}]", opinions.size(), minStars, maxStars);
        return opinions;
    }

    @Override
    public List<Opinion> findAll(String productCode) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(simpleDriverDataSource);

        List<Opinion> opinions = jdbcTemplate.query(
                SELECT_ALL_OPINION_WHERE_PRODUCT_CODE,
                Map.of("productCode", productCode),
                (rs, rowNum) -> databaseMapper.mapOpinion(rs, rowNum));

        log.info("Found: [{}] opinions with product code: [{}]", opinions.size(), productCode);
        return opinions;

    }

    @Override
    public int removeAll(String email) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(simpleDriverDataSource);

        int removedRows = jdbcTemplate.update(
                DELETE_FROM_OPINION_WHERE_EMAIL,
                Map.of("email", email));

        log.info("Removed opinion rows for customer with email: [{}]", removedRows);
        return removedRows;
    }

    @Override
    public int removeAll(int minStars, int maxStars) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(simpleDriverDataSource);
        int removedRows = jdbcTemplate.update(
                DELETE_FROM_OPINION_WHERE_STARS_BETWEEN,
                Map.of("min", minStars, "max", maxStars));

        log.info("Removed opinion rows: [{}] for opinions with stars between [{}] and [{}]",
                removedRows,
                minStars,
                maxStars);
        return removedRows;
    }

    @Override
    public void removeAllForPurchasesWithProductCode(String productCode) {
        new NamedParameterJdbcTemplate(simpleDriverDataSource).update(
                REMOVE_ALL_OPINION_WHERE_PRODUCT_CODE,
                Map.of("productCode", productCode)
        );
    }
}
