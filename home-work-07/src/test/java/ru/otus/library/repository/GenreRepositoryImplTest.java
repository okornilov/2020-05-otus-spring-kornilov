package ru.otus.library.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.library.domain.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование GenreRepositoryImpl")
@DataJpaTest
@Import(TestRepositoryConfiguration.class)
class GenreRepositoryImplTest {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("Добавить жанр")
    @Test
    void createGenre() {
        Genre genre = Genre.builder().name("Genre 1").build();
        genreRepository.save(genre);

        assertThat(em.find(Genre.class, genre.getId()))
                .isEqualToComparingFieldByField(genre);
    }

    @DisplayName("Изменить жанр")
    @Test
    void updateGenre() {
        Genre genre = Genre.builder().name("Genre 2").build();
        em.persist(genre);

        genre.setName("Genre New");
        genreRepository.save(genre);

        assertThat(em.find(Genre.class, genre.getId()))
                .isEqualToComparingFieldByField(genre);
    }

    @DisplayName("Найти все жанры")
    @Test
    void findAll() {
        Genre genre1 = Genre.builder().name("Genre 1").build();
        Genre genre2 = Genre.builder().name("Genre 2").build();

        em.persist(genre1);
        em.persist(genre2);

        assertThat(genreRepository.findAll())
                .hasSize(2)
                .containsOnly(genre1, genre2);
    }

    @DisplayName("Найти жанр по идентификатору")
    @Test
    void findById() {
        Genre genre = Genre.builder().name("Genre 3").build();
        em.persist(genre);

        assertThat(genreRepository.findById(genre.getId()))
                .get()
                .isEqualToComparingFieldByField(genre);
    }

    @DisplayName("Удалить жанр по идентификатору")
    @Test
    void deleteById() {
        Genre genre = Genre.builder().name("genreDao 5").build();
        em.persist(genre);
        em.detach(genre);

        genreRepository.deleteById(genre.getId());

        Genre genre2 = em.find(Genre.class, genre.getId());
        assertThat(genre2).isNull();
    }

    @DisplayName("Удалить все жанры")
    @Test
    void deleteAll() {
        em.persist(Genre.builder().name("Genre 6").build());
        em.persist(Genre.builder().name("Genre 7").build());

        genreRepository.deleteAll();

        List<Genre> genreList = em.getEntityManager()
                .createQuery("select g from Genre g", Genre.class)
                .getResultList();

        assertThat(genreList)
                .isEmpty();
    }
}