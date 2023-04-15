package pl.zajavka.domain;

import lombok.*;

import java.time.OffsetDateTime;

@Data
@With
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Purchase {
    private Long id;
    private Customer customerId;
    private Product productId;
    private int quantity;
    private OffsetDateTime dateTime;
}
