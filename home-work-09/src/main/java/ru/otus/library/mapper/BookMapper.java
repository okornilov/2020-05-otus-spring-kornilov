package ru.otus.library.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;
import ru.otus.library.dto.BookDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "authors", qualifiedByName = "authors")
    @Mapping(target = "genres", qualifiedByName = "genres")
    BookDto toDto(Book book);

    @Named("authors")
    default String authorString(List<Author> authors) {
        return Optional.ofNullable(authors)
                .orElse(List.of())
                .stream()
                .map(Author::getFullName)
                .collect(Collectors.joining(", "));
    }

    @Named("genres")
    default String genreString(List<Genre> genres) {
        return Optional.ofNullable(genres)
                .orElse(List.of())
                .stream()
                .map(Genre::getName)
                .collect(Collectors.joining(", "));
    }
}
