package pl.zajavka.infrastructure.configuration;

import org.postgresql.Driver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class DatabaseConfiguration {
    public static final String CUSTOMER_TABLE = "customer";
    public static final String CUSTOMER_TABLE_PKEY = "id";
    public static final String PRODUCER_TABLE = "producer";
    public static final String PRODUCER_TABLE_PKEY = "id";
    public static final String PURCHASE_TABLE = "purchase";
    public static final String PURCHASE_TABLE_PKEY = "id";
    public static final String PRODUCT_TABLE = "product";
    public static final String PRODUCT_TABLE_PKEY = "id";
    public static final String OPINION_TABLE = "opinion";
    public static final String OPINION_TABLE_PKEY = "id";

    public static final String DELETE_FROM_CUSTOMER = "DELETE FROM customer WHERE 1 = 1";
    public static final String SELECT_ONE_USER_WHERE_EMAIL = "SELECT * FROM customer WHERE email = :email";
    public static final String SELECT_ONE_PRODUCER_WHERE_ID = "SELECT * FROM producer WHERE id = :id";
    public static final String SELECT_ONE_PRODUCER_WHERE_NAME = "SELECT * FROM producer WHERE producer_name = :producer_name";
    public static final String SELECT_ALL_CUSTOMERS = "SELECT * FROM customer";

    @Bean
    public SimpleDriverDataSource simpleDriverDataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriver(new Driver());
        dataSource.setUrl("jdbc:postgresql://localhost:5432/store_test");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres");
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager txManager() {
        return new DataSourceTransactionManager(simpleDriverDataSource());
    }
}
