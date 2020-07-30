package ru.otus.library.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование AuthorRepository")
@DataMongoTest
@Import(TestRepositoryConfiguration.class)
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        mongoTemplate.dropCollection(Book.class);
    }

    @DisplayName("Добавить автора в книгу")
    @Test
    void addTest() {
        Book book =  mongoTemplate.insert(Book.builder()
                .name("Test book 2")
                .authors(List.of(Author.builder()
                        .id(UUID.randomUUID().toString())
                        .fullName("author 1").build()))
                .build());

        Author author = Author.builder()
                .fullName("NewAuthor")
                .build();
        authorRepository.save(book.getId(), author);

        Book foundBook = mongoTemplate.findOne(new Query(Criteria
                .where("_id").is(book.getId())
                .and("authors.fullName").is(author.getFullName())), Book.class);
        assert foundBook != null;

        assertThat(foundBook.getAuthors())
                .extracting(Author::getFullName)
                .contains(author.getFullName());
    }

    @DisplayName("Обновить автора в книге")
    @Test
    void updateTest() {
        final Author author = Author.builder()
                .id(UUID.randomUUID().toString())
                .fullName("author 1").build();

        Book book =  mongoTemplate.insert(Book.builder()
                .name("Test book 2")
                .authors(List.of(author))
                .build());

        author.setFullName("newAuthor");

        authorRepository.save(book.getId(), author);

        Book foundBook = mongoTemplate.findOne(new Query(Criteria
                .where("_id").is(book.getId())
                .and("authors._id").is(author.getId())), Book.class);
        assert foundBook != null;

        assertThat(foundBook.getAuthors())
                .extracting(Author::getFullName)
                .containsOnly(author.getFullName());
    }

    @DisplayName("Получить всех авторов по книге")
    @Test
    void findByBookIdTest() {
        List<Author> authors = List.of(Author.builder()
                .id(UUID.randomUUID().toString())
                .fullName("Author1").build());

        Book book = Book.builder()
                .name("Test book 1")
                .authors(authors)
                .build();
        mongoTemplate.insert(book);

        Page<Author> page = authorRepository.findAllByBookId(book.getId(), PageRequest.of(0, 10));

        assertThat(page.getContent())
                .extracting(Author::getFullName)
                .containsExactlyElementsOf(authors.stream().map(Author::getFullName).collect(Collectors.toList()));
    }

    @DisplayName("Получить автора по идентификатору")
    @Test
    void findByIdTest() {
        final Author expectedAuthor = Author.builder()
                .id(UUID.randomUUID().toString())
                .fullName("Author1").build();

        Book book = Book.builder()
                .name("Test book 1")
                .authors(List.of(expectedAuthor))
                .build();

        mongoTemplate.insert(book);

        final Optional<Author> author = authorRepository.findById(expectedAuthor.getId());

        assertThat(author)
                .get()
                .isEqualTo(expectedAuthor);
    }

    @DisplayName("Удалить автора")
    @Test
    void deleteByIdTest() {
        final Author author = Author.builder()
                .id(UUID.randomUUID().toString())
                .fullName("Author1").build();

        Book book = Book.builder()
                .name("Test book 1")
                .authors(List.of(author))
                .build();

        mongoTemplate.insert(book);
        authorRepository.deleteById(author.getId());

        assertThat(mongoTemplate.find(new Query(Criteria.where("authors._id").is(author.getId())), Book.class))
                .isEmpty();
    }
}
