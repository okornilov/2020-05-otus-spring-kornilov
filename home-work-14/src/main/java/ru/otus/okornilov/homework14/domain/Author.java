package ru.otus.okornilov.homework14.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Author {
    private String id;
    private String fullName;
    private String bookId;
}
