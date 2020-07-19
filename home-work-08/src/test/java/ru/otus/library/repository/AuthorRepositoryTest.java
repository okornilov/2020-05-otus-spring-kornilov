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
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.dto.AuthorDto;
import ru.otus.library.dto.CountDto;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

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
        authorRepository.add(book.getId(), author);

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
        Book book =  mongoTemplate.insert(Book.builder()
                .name("Test book 2")
                .authors(List.of(Author.builder()
                        .id(UUID.randomUUID().toString())
                        .fullName("author 1").build()))
                .build());

        final Author author1 = book.getAuthors().stream().findFirst().orElseThrow(RuntimeException::new);
        final Author newAuthor = Author.builder().fullName("newAuthor").build();

        authorRepository.update(author1.getId(), newAuthor);

        Book foundBook = mongoTemplate.findOne(new Query(Criteria
                .where("_id").is(book.getId())
                .and("authors._id").is(author1.getId())), Book.class);
        assert foundBook != null;

        assertThat(foundBook.getAuthors())
                .extracting(Author::getFullName)
                .contains(newAuthor.getFullName());
    }

    @DisplayName("Получить всех авторов")
    @Test
    void findAllTest() {
        List<Author> authors1 = List.of(Author.builder()
                .id(UUID.randomUUID().toString())
                .fullName("Ivanov").build());

        List<Author> authors2 = List.of(Author.builder()
                .id(UUID.randomUUID().toString())
                .fullName("Petrov").build());

        mongoTemplate.insert(Book.builder()
                .name("Test book 1")
                .authors(authors1)
                .build());
        mongoTemplate.insert(Book.builder()
                .name("Test book 2")
                .authors(authors2)
                .build());

        Page<AuthorDto> page = authorRepository.findAll(PageRequest.of(0, 10));

        assertThat(page.getContent())
                .extracting(AuthorDto::getFullName)
                .containsAll(authors1.stream().map(Author::getFullName).collect(Collectors.toList()))
                .containsAll(authors2.stream().map(Author::getFullName).collect(Collectors.toList()));
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

        Page<AuthorDto> page = authorRepository.findByBookId(book.getId(), PageRequest.of(0, 10));

        assertThat(page.getContent())
                .extracting(AuthorDto::getFullName)
                .containsExactlyElementsOf(authors.stream().map(Author::getFullName).collect(Collectors.toList()));
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

    @DisplayName("Удалить всех авторов")
    @Test
    void deleteAllTest() {
        List<Author> authors1 = List.of(Author.builder()
                .id(UUID.randomUUID().toString())
                .fullName("Test1").build());

        List<Author> authors2 = List.of(Author.builder()
                .id(UUID.randomUUID().toString())
                .fullName("Test2").build());

        mongoTemplate.insert(Book.builder()
                .name("Test book")
                .authors(authors1)
                .build());
        mongoTemplate.insert(Book.builder()
                .name("Test book")
                .authors(authors2)
                .build());

        authorRepository.deleteAll();

        Aggregation aggregation = newAggregation(Aggregation.unwind("authors"), Aggregation.count().as("total"));
        CountDto countDto = mongoTemplate.aggregate(aggregation, Book.class, CountDto.class)
                .getUniqueMappedResult();

        assertThat(countDto) //будет null если нет элементов
            .isNull();
    }
}
