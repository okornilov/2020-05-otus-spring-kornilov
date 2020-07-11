package ru.otus.library.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.library.domain.Comment;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.library.util.TestUtils.createBook;
import static ru.otus.library.util.TestUtils.createComment;

@DisplayName("Тестирование CommentRepositoryImpl")
@DataJpaTest
@Import(TestRepositoryConfiguration.class)
class CommentRepositoryImplTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TestEntityManager em;


    @DisplayName("Создать комментарий")
    @Test
    void createCommentTest() {
        Comment comment = createComment("Comment1", createBook("Book1", "Author1", "Genre1"));

        commentRepository.save(comment);

        assertThat(em.find(Comment.class, comment.getId()))
                .isEqualToComparingFieldByField(comment);
    }

    @DisplayName("Обновить комментарий")
    @Test
    void updateCommentTest() {
        Comment comment = createComment("Comment2", createBook("Book2", "Author2", "Genre2"));
        em.persist(comment);

        comment.setComment("CommentNew");
        commentRepository.save(comment);

        assertThat(em.find(Comment.class, comment.getId()))
                .isEqualToComparingFieldByField(comment);
    }

    @DisplayName("Найти все комментарии")
    @Test
    void findAllTest() {
        Comment comment3 = createComment("Comment3", createBook("Book3", "Author3", "Genre3"));
        Comment comment4 = createComment("Comment4", createBook("Book4", "Author4", "Genre4"));

        em.persist(comment3);
        em.persist(comment4);

        assertThat(commentRepository.findAll())
                .containsOnly(comment3, comment4);
    }

    @DisplayName("Найти комментарий по идентификатору")
    @Test
    void findByIdTest() {
        Comment comment = createComment("Comment5", createBook("Book5", "Author5", "Genre5"));
        em.persist(comment);

        assertThat(commentRepository.findById(comment.getId()))
                .get()
                .isEqualToComparingFieldByField(comment);
    }

    @DisplayName("Удалить комментарий по идентификатору")
    @Test
    void deleteByIdTest() {
        Comment comment = createComment("Comment6", createBook("Book6", "Author6", "Genre6"));
        em.persist(comment);
        em.detach(comment);

        commentRepository.deleteById(comment.getId());

        assertThat(em.find(Comment.class ,comment.getId()))
                .isNull();
    }

    @DisplayName("Удалить все комментарии")
    @Test
    void deleteAllTest() {
        em.persist(createComment("Comment7", createBook("Book7", "Author7", "Genre7")));
        em.persist(createComment("Comment8", createBook("Book8", "Author8", "Genre8")));

        commentRepository.deleteAll();

        assertThat(em.getEntityManager().createQuery("select c from Comment c", Comment.class).getResultList())
                .isEmpty();
    }
}