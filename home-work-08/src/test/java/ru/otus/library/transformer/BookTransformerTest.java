package ru.otus.library.transformer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = BookTransformer.class)
@DisplayName("Тестирование BookTransformer")
class BookTransformerTest {

    @Autowired
    BookTransformer bookTransformer;

    @Test
    void transformTest() {
        final String[] authors = {"a1", "a2"};
        final String[] genres = {"g1", "g2"};

        Book book = Book.builder()
                .id("1")
                .name("book")
                .authors(Arrays.stream(authors).map(s -> Author.builder().fullName(s).build()).collect(Collectors.toList()))
                .genres(Arrays.stream(genres).map(s -> Genre.builder().name(s).build()).collect(Collectors.toList()))
                .build();

        Book bookNew = bookTransformer.transform(book.getId(), book.getName(), authors, genres);

        assertThat(bookNew).extracting(Book::getName).isEqualTo(book.getName());
        assertThat(bookNew.getAuthors()).extracting(Author::getFullName).containsOnly(authors);
        assertThat(bookNew.getGenres()).extracting(Genre::getName).containsOnly(genres);
    }
}