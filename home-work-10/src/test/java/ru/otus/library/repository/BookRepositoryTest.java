package ru.otus.library.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Comment;
import ru.otus.library.domain.Genre;

import java.util.List;
import java.util.UUID;

@DisplayName("Тестирование BookRepository")
@DataMongoTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @DisplayName("Сохранить книгу")
    @Test
    void createTest() {
        Book book = Book.builder()
                .name("test book")
                .authors(List.of(Author.builder()
                        .id(UUID.randomUUID().toString())
                        .fullName("author 1").build()))
                .genres(List.of(Genre.builder()
                        .id(UUID.randomUUID().toString())
                        .name("genre 1").build()))
                .comments(List.of(Comment.builder()
                        .id(UUID.randomUUID().toString())
                        .text("comment1").build()))
                .build();
        bookRepository.save(book);
        Assertions.assertThat(bookRepository.findById(book.getId()))
                .get()
                .isEqualToComparingFieldByField(book);
    }
}