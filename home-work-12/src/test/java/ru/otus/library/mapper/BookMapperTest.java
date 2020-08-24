package ru.otus.library.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;
import ru.otus.library.dto.BookDto;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = BookMapperImpl.class)
class BookMapperTest {

    @Autowired
    BookMapper bookMapper;

    @Test
    void toDto() {
        final Book book = Book.builder()
                .id("1")
                .name("Book")
                .authors(List.of(Author.builder()
                        .id("2")
                        .fullName("Author")
                        .build()))
                .genres(List.of(Genre.builder()
                        .id("3")
                        .name("Genre")
                        .build()))
                .build();
        final BookDto bookDto = bookMapper.toDto(book);

        assertThat(bookDto)
                .extracting(BookDto::getName)
                .isEqualTo(book.getName());

        assertThat(bookDto)
                .extracting(BookDto::getAuthors)
                .isEqualTo(book.getAuthors().stream()
                        .map(Author::getFullName)
                        .collect(Collectors.joining(", ")));

        assertThat(bookDto)
                .extracting(BookDto::getGenres)
                .isEqualTo(book.getGenres().stream()
                        .map(Genre::getName)
                        .collect(Collectors.joining(", ")));
    }
}