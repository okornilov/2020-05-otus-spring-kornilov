package ru.otus.library.domain;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode
public class Book {
    private Long id;
    private String name;
    private Author author;
    private Genre genre;
}
