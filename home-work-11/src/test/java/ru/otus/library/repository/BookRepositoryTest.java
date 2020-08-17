package ru.otus.library.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.test.StepVerifier;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;
import ru.otus.library.dto.Page;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Тестирование BookRepository")
@DataMongoTest
@Import(TestRepositoryConfiguration.class)
class BookRepositoryTest {

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        reactiveMongoTemplate.dropCollection(Book.class).block();
    }

    @DisplayName("Сохранить книгу")
    @Test
    void saveTest() {
        final Book book = Book.builder()
                .name("BookName")
                .genres(List.of(Genre.builder().name("GenreName").build()))
                .authors(List.of(Author.builder().fullName("AuthorFullName").build()))
                .build();

        StepVerifier
                .create(bookRepository.save(book))
                .assertNext(b -> assertThat(b).isEqualToComparingFieldByField(book))
                .expectComplete()
                .verify();
    }

    @DisplayName("Получить все книги с пагинацией")
    @Test
    void findAllTest() {
        final Book book1 = Book.builder().name("book1").build();
        final Book book2 = Book.builder().name("book2").build();
        final Book book3 = Book.builder().name("book3").build();
        final Book book4 = Book.builder().name("book4").build();
        final List<Book> books = List.of(book1, book2, book3, book4);

        StepVerifier.create(reactiveMongoTemplate.insertAll(books))
                .expectNextCount(books.size())
                .expectComplete()
                .verify();

        final PageRequest pageable1 = PageRequest.of(0, 2);
        StepVerifier
                .create(bookRepository.findAll(pageable1))
                .assertNext(page -> {
                    Page<Book> expectedPage = new Page<>(List.of(book1, book2), pageable1.getPageNumber(), page.getPageSize(), books.size());
                    assertThat(page).isEqualToComparingFieldByField(expectedPage);
                })
                .expectComplete()
                .verify();

        final PageRequest pageable2 = PageRequest.of(1, 2);
        StepVerifier
                .create(bookRepository.findAll(pageable2))
                .assertNext(page -> {
                    Page<Book> expectedPage = new Page<>(List.of(book3, book4), pageable2.getPageNumber(), page.getPageSize(), books.size());
                    assertThat(page).isEqualToComparingFieldByField(expectedPage);
                })
                .expectComplete()
                .verify();

    }
}