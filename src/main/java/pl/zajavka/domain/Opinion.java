package pl.zajavka.domain;

import lombok.*;

import java.time.OffsetDateTime;

@Data
@With
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Opinion {
    private Long id;
    private Customer customerId;
    private Product productId;
    private byte stars;
    private String comment;
    private OffsetDateTime dateTime;
}
