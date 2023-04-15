package pl.zajavka.domain;

import lombok.*;

import java.time.LocalDate;

@Data
@With
@Builder
@ToString
@AllArgsConstructor
public class Customer {
    private Long id;
    private String userName;
    private String email;
    private String name;
    private String surname;
    private LocalDate dateOfBirth;
    private String telephoneNumber;
}
