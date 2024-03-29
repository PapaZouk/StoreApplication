package pl.zajavka.domain;

import lombok.*;

@Data
@With
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Producer {
    private Long id;
    private String producerName;
    private String address;
}