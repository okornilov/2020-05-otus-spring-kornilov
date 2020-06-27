package ru.otus.library.domain;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode
public class Genre {
    private Long id;
    private String name;
}
