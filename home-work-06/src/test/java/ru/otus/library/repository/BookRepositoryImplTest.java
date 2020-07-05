package ru.otus.library.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.library.domain.Book;
import ru.otus.library.util.TestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование BookRepositoryImpl")
@DataJpaTest
@Import(TestRepositoryConfiguration.class)
class BookRepositoryImplTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("Добавить книгу")
    @Test
    void create() {
        Book book = TestUtils.createBook("Book 1", "Ivanov 1", "Genre 1");

        bookRepository.save(book);

        assertThat(em.find(Book.class, book.getId()))
                .isEqualToComparingFieldByField(book);
    }

    @DisplayName("Изменить книгу")
    @Test
    void update() {
        Book book = TestUtils.createBook("Book 1", "Ivanov 1", "Genre 1");
        em.persist(book);

        book.setName("Book New");
        bookRepository.save(book);

        assertThat(em.find(Book.class, book.getId()))
                .isEqualToComparingFieldByField(book);
    }

    @DisplayName("Удалить книгу по идентификатору")
    @Test
    void deleteById() {
        Book book = TestUtils.createBook("Book 2", "Ivanov 2", "Genre 2");
        em.persist(book);
        em.detach(book);

        bookRepository.deleteById(book.getId());

        assertThat(em.find(Book.class, book.getId()))
                .isNull();
    }

    @DisplayName("Удалить все книги")
    @Test
    void deleteAll() {
        em.persist(TestUtils.createBook("Book 3", "Ivanov 3", "Genre 3"));
        em.persist(TestUtils.createBook("Book 4", "Ivanov 4", "Genre 4"));
        bookRepository.deleteAll();

        assertThat(em.getEntityManager().createQuery("select b from Book b", Book.class).getResultList())
                .isEmpty();
    }

    @DisplayName("Найти все книги")
    @Test
    void findAll() {
        Book book5 = TestUtils.createBook("Book 5", "Ivanov 5", "Genre 5");
        Book book6 = TestUtils.createBook("Book 6", "Ivanov 6", "Genre 6");

        em.persist(book5);
        em.persist(book6);

        Assertions.assertThat(bookRepository.findAll())
                .contains(book5, book6);
    }

    @DisplayName("Найти книгу по идентификатору")
    @Test
    void findById() {
        Book book8 = TestUtils.createBook("Book 8", "Ivanov 8", "Genre 8");
        em.persist(book8);
        Optional<Book> optionalBook = bookRepository.findById(book8.getId());

        Assertions.assertThat(optionalBook).get()
                .isEqualTo(book8);
    }
}