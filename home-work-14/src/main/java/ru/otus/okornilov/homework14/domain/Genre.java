package ru.otus.okornilov.homework14.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Genre {
    private String id;
    private String name;
    private String bookId;
}
