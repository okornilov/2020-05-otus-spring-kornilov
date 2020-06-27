package ru.otus.library.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.domain.Genre;
import ru.otus.library.exceptions.EntityNotFound;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Тестирование GenreDaoImpl")
@JdbcTest
@Transactional
@Import(TestDaoConfiguration.class)
class GenreDaoImplTest {

    @Autowired
    private GenreDao genreDao;

    @DisplayName("Добавить жанр")
    @Test
    void createGenre() {
        Genre genre = Genre.builder().name("Genre 1").build();
        genreDao.save(genre);

        assertThat(genreDao.findById(genre.getId()))
                .get()
                .isEqualToComparingFieldByField(genre);
    }

    @DisplayName("Изменить жанр")
    @Test
    void updateGenre() {
        Genre genre = Genre.builder().name("Genre 2").build();
        genreDao.save(genre);

        genre.setName("Genre New");
        genreDao.save(genre);

        assertThat(genreDao.findById(genre.getId()))
                .get()
                .isEqualToComparingFieldByField(genre);
    }

    @DisplayName("Изменить несуществующий жанр")
    @Test
    void updateGenreError() {
        Genre genre = Genre.builder().id(1L).name("Genre").build();
        assertThatThrownBy(() -> genreDao.save(genre))
                .isInstanceOf(EntityNotFound.class);

    }

    @DisplayName("Найти все жанры")
    @Test
    void findAll() {
        Genre genre1 = Genre.builder().name("Genre 1").build();
        Genre genre2 = Genre.builder().name("Genre 2").build();

        genreDao.save(genre1);
        genreDao.save(genre2);

        assertThat(genreDao.findAll())
                .containsOnly(genre1, genre2);
    }

    @DisplayName("Найти жанр по идентификатору")
    @Test
    void findById() {
        Genre genre = Genre.builder().name("Genre 3").build();
        genreDao.save(genre);

        assertThat(genreDao.findById(genre.getId()))
                .get()
                .isEqualToComparingFieldByField(genre);
    }

    @DisplayName("Найти жанр по наименованию")
    @Test
    void findByName() {
        Genre genre = Genre.builder().name("Genre 4").build();
        genreDao.save(genre);

        assertThat(genreDao.findByName(genre.getName()))
                .get()
                .isEqualToComparingFieldByField(genre);
    }

    @DisplayName("Удалить жанр по идентификатору")
    @Test
    void deleteById() {
        Genre genre = Genre.builder().name("genreDao 5").build();
        genreDao.save(genre);
        genreDao.deleteById(genre.getId());

        Optional<Genre> optionalGenre = genreDao.findById(genre.getId());

        assertThat(optionalGenre)
                .isEmpty();
    }

    @DisplayName("Удалить все жанры")
    @Test
    void deleteAll() {
        genreDao.save(Genre.builder().name("Genre 6").build());
        genreDao.save(Genre.builder().name("Genre 7").build());

        genreDao.deleteAll();

        assertThat(genreDao.findAll())
                .isEmpty();
    }
}