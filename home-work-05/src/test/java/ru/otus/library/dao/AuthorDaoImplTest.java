package ru.otus.library.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.library.domain.Author;
import ru.otus.library.exceptions.EntityNotFound;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Тестирование AuthorDaoImpl")
@JdbcTest
@Import(TestDaoConfiguration.class)
class AuthorDaoImplTest {

    @Autowired
    private AuthorDao authorDao;

    @DisplayName("Добавить автора")
    @Test
    void createAuthor() {
        Author author = Author.builder().fio("Ivanov 1").build();
        authorDao.save(author);

        assertThat(authorDao.findById(author.getId()))
                .get()
                .isEqualToComparingFieldByField(author);
    }

    @DisplayName("Обновить автора")
    @Test
    void updateAuthor() {
        Author author = Author.builder().fio("Ivanov 2").build();
        authorDao.save(author);

        author.setFio("Ivanov New");
        authorDao.save(author);

        assertThat(authorDao.findById(author.getId()))
                .get()
                .isEqualToComparingFieldByField(author);
    }

    @DisplayName("Обновить несуществующего автора")
    @Test
    void updateAuthorError() {
        Author author = Author.builder().id(1L).fio("Ivanov 2").build();
        assertThatThrownBy(() -> authorDao.save(author))
            .isInstanceOf(EntityNotFound.class);

    }

    @DisplayName("Найти всех авторов")
    @Test
    void findAll() {
        Author author1 = Author.builder().fio("Petrov 1").build();
        Author author2 = Author.builder().fio("Petrov 2").build();

        authorDao.save(author1);
        authorDao.save(author2);

        Assertions.assertThat(authorDao.findAll())
                .containsOnly(author1, author2);
    }

    @DisplayName("Найти автора по идентификатору")
    @Test
    void findById() {
        Author author = Author.builder().fio("Ivanov 3").build();
        authorDao.save(author);

        assertThat(authorDao.findById(author.getId()))
                .get()
                .isEqualToComparingFieldByField(author);
    }

    @DisplayName("Найти автора по ФИО")
    @Test
    void findByFio() {
        Author author = Author.builder().fio("Ivanov 4").build();
        authorDao.save(author);

        assertThat(authorDao.findByFio(author.getFio()))
                .get()
                .isEqualToComparingFieldByField(author);
    }

    @DisplayName("Удалить автора по идентификатору")
    @Test
    void deleteById() {
        Author author = Author.builder().fio("Ivanov 5").build();
        authorDao.save(author);
        authorDao.deleteById(author.getId());

        Optional<Author> optionalAuthor = authorDao.findById(author.getId());

        assertThat(optionalAuthor)
                .isEmpty();
    }

    @DisplayName("Удалить всех авторов")
    @Test
    void deleteAll() {
        authorDao.save(Author.builder().fio("Ivanov 6").build());
        authorDao.save(Author.builder().fio("Ivanov 7").build());

        authorDao.deleteAll();

        assertThat(authorDao.findAll())
                .isEmpty();
    }
}