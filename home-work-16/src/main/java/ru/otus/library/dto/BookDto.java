package ru.otus.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BookDto {
    private String id;
    @NotBlank
    private String name;
    @Valid
    private List<AuthorDto> authors;
    @Valid
    private List<GenreDto> genres;
}
