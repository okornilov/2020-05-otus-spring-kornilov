package ru.otus.library.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.library.domain.Author;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование AuthorRepositoryImpl")
@DataJpaTest
@Import(TestRepositoryConfiguration.class)
class AuthorRepositoryImplTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("Добавить автора")
    @Test
    void createAuthor() {
        Author author = Author.builder().fullName("Ivanov 1").build();
        authorRepository.save(author);

        assertThat(em.find(Author.class, author.getId()))
                .isEqualToComparingFieldByField(author);
    }

    @DisplayName("Обновить автора")
    @Test
    void updateAuthor() {
        Author author = Author.builder().fullName("Ivanov 2").build();
        em.persist(author);

        author.setFullName("Ivanov New");
        authorRepository.save(author);

        assertThat(em.find(Author.class, author.getId()))
                .isEqualToComparingFieldByField(author);
    }

    @DisplayName("Найти всех авторов")
    @Test
    void findAll() {
        Author author1 = Author.builder().fullName("Petrov 1").build();
        Author author2 = Author.builder().fullName("Petrov 2").build();

        em.persist(author1);
        em.persist(author2);

        Assertions.assertThat(authorRepository.findAll())
                .hasSize(2)
                .containsOnly(author1, author2);
    }

    @DisplayName("Найти автора по идентификатору")
    @Test
    void findById() {
        Author author = Author.builder().fullName("Ivanov 3").build();
        em.persist(author);

        assertThat(authorRepository.findById(author.getId()))
                .get()
                .isEqualToComparingFieldByField(author);
    }


    @DisplayName("Удалить автора по идентификатору")
    @Test
    void deleteById() {
        Author author = Author.builder().fullName("Ivanov 5").build();
        em.persist(author);
        em.detach(author);

        authorRepository.deleteById(author.getId());

        assertThat(em.find(Author.class, author.getId()))
                .isNull();
    }

    @DisplayName("Удалить всех авторов")
    @Test
    void deleteAll() {
        em.persist(Author.builder().fullName("Ivanov 6").build());
        em.persist(Author.builder().fullName("Ivanov 7").build());

        authorRepository.deleteAll();

        List<Author> authorList = em.getEntityManager().createQuery("select a from Author a", Author.class).getResultList();
        assertThat(authorList)
                .isEmpty();
    }
}