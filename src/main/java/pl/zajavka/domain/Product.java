package pl.zajavka.domain;

import lombok.*;

import java.math.BigDecimal;

@Data
@With
@Builder
@ToString
@AllArgsConstructor
public class Product {
    private Long id;
    private String productCode;
    private String productName;
    private BigDecimal productPrice;
    private boolean adultsOnly;
    private String description;
    private Producer producerId;
}
