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
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;
import ru.otus.library.dto.CountDto;
import ru.otus.library.dto.GenreDto;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

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
        genreRepository.add(book.getId(), genre);

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
        Book book =  mongoTemplate.insert(Book.builder()
                .name("Test book 2")
                .genres(List.of(Genre.builder()
                        .id(UUID.randomUUID().toString())
                        .name("genre 1").build()))
                .build());

        final Genre genre1 = book.getGenres().stream().findFirst().orElseThrow(RuntimeException::new);
        final Genre newGenre = Genre.builder().name("newGenre").build();

        genreRepository.update(genre1.getId(), newGenre);

        Book foundBook = mongoTemplate.findOne(new Query(Criteria
                .where("_id").is(book.getId())
                .and("genres._id").is(genre1.getId())), Book.class);
        assert foundBook != null;

        assertThat(foundBook.getGenres())
                .extracting(Genre::getName)
                .contains(newGenre.getName());
    }

    @DisplayName("Получить все жанры")
    @Test
    void findAllTest() {
        List<Genre> genres1 = List.of(Genre.builder()
                .id(UUID.randomUUID().toString())
                .name("Genre1").build());

        List<Genre> genres2 = List.of(Genre.builder()
                .id(UUID.randomUUID().toString())
                .name("Genre2").build());

        mongoTemplate.insert(Book.builder()
                .name("Test book 1")
                .genres(genres1)
                .build());

        mongoTemplate.insert(Book.builder()
                .name("Test book 2")
                .genres(genres2)
                .build());

        Page<GenreDto> page = genreRepository.findAll(PageRequest.of(0, 10));

        assertThat(page.getContent())
                .extracting(GenreDto::getName)
                .containsAll(genres1.stream().map(Genre::getName).collect(Collectors.toList()))
                .containsAll(genres1.stream().map(Genre::getName).collect(Collectors.toList()));
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

        Page<GenreDto> page = genreRepository.findByBookId(book.getId(), PageRequest.of(0, 10));

        assertThat(page.getContent())
                .extracting(GenreDto::getName)
                .containsExactlyElementsOf(genres.stream().map(Genre::getName).collect(Collectors.toList()));
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

    @DisplayName("Удалить все жанры")
    @Test
    void deleteAllTest() {
        List<Genre> genres1 = List.of(Genre.builder()
                .id(UUID.randomUUID().toString())
                .name("Genre1").build());

        List<Genre> genres2 = List.of(Genre.builder()
                .id(UUID.randomUUID().toString())
                .name("Genre2").build());

        mongoTemplate.insert(Book.builder()
                .name("Test book")
                .genres(genres1)
                .build());
        mongoTemplate.insert(Book.builder()
                .name("Test book")
                .genres(genres2)
                .build());

        genreRepository.deleteAll();

        Aggregation aggregation = newAggregation(Aggregation.unwind("genres"), Aggregation.count().as("total"));
        CountDto countDto = mongoTemplate.aggregate(aggregation, Book.class, CountDto.class)
                .getUniqueMappedResult();

        assertThat(countDto) //будет null если нет элементов
            .isNull();
    }
}
