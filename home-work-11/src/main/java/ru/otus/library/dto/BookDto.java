package ru.otus.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BookDto {
    private String id;
    private String name;
    private List<AuthorDto> authors;
    private List<GenreDto> genres;
}
