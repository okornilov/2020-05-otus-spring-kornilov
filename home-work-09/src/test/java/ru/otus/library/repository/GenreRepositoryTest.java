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
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Comment;
import ru.otus.library.domain.Genre;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование GenreRepository")
@DataMongoTest
@Import(TestRepositoryConfiguration.class)
class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        mongoTemplate.dropCollection(Book.class);
    }

    @DisplayName("Добавить жанр в книгу")
    @Test
    void addTest() {
        Book book =  mongoTemplate.insert(Book.builder()
                .name("Test book 2")
                .genres(List.of(Genre.builder()
                        .id(UUID.randomUUID().toString())
                        .name("genre 1").build()))
                .build());

        Genre genre = Genre.builder()
                .name("NewGenre")
                .build();
        genreRepository.save(book.getId(), genre);

        Book foundBook = mongoTemplate.findOne(new Query(Criteria
                .where("_id").is(book.getId())
                .and("genres.name").is(genre.getName())), Book.class);
        assert foundBook != null;

        assertThat(foundBook.getGenres())
                .extracting(Genre::getName)
                .contains(genre.getName());
    }

    @DisplayName("Обновить жанр в книге")
    @Test
    void updateTest() {
        final Genre genre = Genre.builder()
                .id(UUID.randomUUID().toString())
                .name("genre 1").build();

        final Book book =  mongoTemplate.insert(Book.builder()
                .name("Test book 2")
                .genres(List.of(genre))
                .build());

        genre.setName("newGenre");

        genreRepository.save(book.getId(), genre);

        Book foundBook = mongoTemplate.findOne(new Query(Criteria
                .where("_id").is(book.getId())
                .and("genres._id").is(genre.getId())), Book.class);
        assert foundBook != null;

        assertThat(foundBook.getGenres())
                .extracting(Genre::getName)
                .containsOnly(genre.getName());
    }

    @DisplayName("Получить всех авторов по книге")
    @Test
    void findByBookIdTest() {
        List<Genre> genres = List.of(Genre.builder()
                .id(UUID.randomUUID().toString())
                .name("Genre1").build());

        Book book = Book.builder()
                .name("Test book 1")
                .genres(genres)
                .build();
        mongoTemplate.insert(book);

        Page<Genre> page = genreRepository.findByBookId(book.getId(), PageRequest.of(0, 10));

        assertThat(page.getContent())
                .extracting(Genre::getName)
                .containsExactlyElementsOf(genres.stream().map(Genre::getName).collect(Collectors.toList()));
    }

    @DisplayName("Получить жанр по идентификатору")
    @Test
    void findByIdTest() {
        final Genre expectedGenre = Genre.builder()
                .id(UUID.randomUUID().toString())
                .name("Genre1").build();

        Book book = Book.builder()
                .name("Test book 1")
                .genres(List.of(expectedGenre))
                .build();

        mongoTemplate.insert(book);

        final Optional<Genre> genre = genreRepository.findById(expectedGenre.getId());

        assertThat(genre)
                .get()
                .isEqualTo(expectedGenre);
    }

    @DisplayName("Удалить жанр")
    @Test
    void deleteByIdTest() {
        final Genre genre = Genre.builder()
            .id(UUID.randomUUID().toString())
            .name("Genre1").build();

        Book book = Book.builder()
                .name("Test book 1")
                .genres(List.of(genre))
                .build();

        mongoTemplate.insert(book);
        genreRepository.deleteById(genre.getId());

        assertThat(mongoTemplate.find(new Query(Criteria.where("genres._id").is(genre.getId())), Book.class))
                .isEmpty();
    }
}
