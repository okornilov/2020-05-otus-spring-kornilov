package ru.otus.library.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;
import ru.otus.library.exceptions.EntityNotFound;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование BookDaoImpl")
@JdbcTest
@Import(TestDaoConfiguration.class)
class BookDaoImplTest {

    @Autowired
    private BookDao bookDao;

    @DisplayName("Добавить книгу")
    @Test
    void create() {
        Book book = createBook("Book 1", "Ivanov 1", "Genre 1");

        bookDao.save(book);
        Optional<Book> optionalBook = bookDao.findById(book.getId());
        assertThat(optionalBook).get()
                .isEqualToComparingFieldByField(book);
    }

    @DisplayName("Изменить книгу")
    @Test
    void update() {
        Book book = createBook("Book 1", "Ivanov 1", "Genre 1");

        bookDao.save(book);
        book.setName("Book New");
        bookDao.save(book);

        Optional<Book> optionalBook = bookDao.findById(book.getId());
        assertThat(optionalBook).get()
                .isEqualToComparingFieldByField(book);
    }

    @DisplayName("Изменить несуществующую книгу")
    @Test
    void updateError() {
        Book book = createBook("Book 1", "Ivanov 1", "Genre 1");
        book.setId(1L);

        Assertions.assertThatThrownBy(() -> bookDao.save(book))
                .isInstanceOf(EntityNotFound.class);
    }

    private Book createBook(String name, String authorFio, String genreName) {
        return Book.builder()
                .name(name)
                .author(Author.builder()
                        .fio(authorFio)
                        .build())
                .genre(Genre.builder()
                        .name(genreName)
                        .build())
                .build();
    }

    @DisplayName("Удалить книгу по идентификатору")
    @Test
    void deleteById() {
        Book book = createBook("Book 2", "Ivanov 2", "Genre 2");
        bookDao.save(book);
        bookDao.deleteById(book.getId());

        Optional<Book> optionalBook = bookDao.findById(book.getId());

        assertThat(optionalBook)
                .isEmpty();
    }

    @DisplayName("Удалить все книги")
    @Test
    void deleteAll() {
        bookDao.save(createBook("Book 3", "Ivanov 3", "Genre 3"));
        bookDao.save(createBook("Book 4", "Ivanov 4", "Genre 4"));
        bookDao.deleteAll();

        assertThat(bookDao.findAll())
                .isEmpty();
    }

    @DisplayName("Найти все книги")
    @Test
    void findAll() {
        Book book5 = createBook("Book 5", "Ivanov 5", "Genre 5");
        Book book6 = createBook("Book 6", "Ivanov 6", "Genre 6");

        bookDao.save(book5);
        bookDao.save(book6);

        bookDao.findAll();

        Assertions.assertThat(bookDao.findAll())
                .contains(book5, book6);
    }

    @DisplayName("Найти книгу по имени")
    @Test
    void findByName() {
        Book book7 = createBook("Book 7", "Ivanov 7", "Genre 7");
        bookDao.save(book7);
        Optional<Book> optionalBook = bookDao.findByName(book7.getName());

        Assertions.assertThat(optionalBook).get()
                .isEqualTo(book7);
    }

    @DisplayName("Найти книгу по идентификатору")
    @Test
    void findById() {
        Book book8 = createBook("Book 8", "Ivanov 8", "Genre 8");
        bookDao.save(book8);
        Optional<Book> optionalBook = bookDao.findById(book8.getId());

        Assertions.assertThat(optionalBook).get()
                .isEqualTo(book8);
    }
}