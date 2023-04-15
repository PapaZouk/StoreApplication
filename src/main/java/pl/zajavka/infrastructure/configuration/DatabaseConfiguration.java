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

    public static final String DELETE_FROM_CUSTOMER = "DELETE FROM customer WHERE 1 = 1";
    public static final String CUSTOMER_TABLE = "customer";
    public static final String CUSTOMER_TABLE_PKEY = "id";
    public static final String SELECT_ONE_USER_WHERE_EMAIL = "SELECT * FROM customer WHERE email = :email";

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