package ru.otus.library.domain;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode
public class Author {
    private Long id;
    private String fio;
}
