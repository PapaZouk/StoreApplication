package pl.zajavka.infrastructure.database;

import org.springframework.stereotype.Component;
import pl.zajavka.domain.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;

@Component
public class DatabaseMapper {

    public Map<String, ?> mapCustomer(Customer customer) {
        return Map.of(
        "userName", customer.getUserName(),
        "email", customer.getEmail(),
        "name", customer.getName(),
        "surname", customer.getSurname(),
        "dateOfBirth", customer.getDateOfBirth(),
        "telephoneNumber", customer.getTelephoneNumber()
        );
    }

    public Customer map(ResultSet resultSet, int rowNum) throws SQLException {
        return Customer.builder()
                .id(resultSet.getLong("id"))
                .userName(resultSet.getString("user_name"))
                .email(resultSet.getString("email"))
                .name(resultSet.getString("name"))
                .surname(resultSet.getString("surname"))
                .dateOfBirth(LocalDate.parse(resultSet.getString("date_of_birth")))
                .telephoneNumber(resultSet.getString("telephone_number"))
                .build();
    }
}
