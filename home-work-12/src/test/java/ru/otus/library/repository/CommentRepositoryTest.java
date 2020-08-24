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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование CommentRepository")
@DataMongoTest
@Import(TestRepositoryConfiguration.class)
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        mongoTemplate.dropCollection(Book.class);
    }

    @DisplayName("Добавить комментарий в книгу")
    @Test
    void addTest() {
        Book book =  mongoTemplate.insert(Book.builder()
                .name("Test book 2")
                .comments(List.of(Comment.builder()
                        .id(UUID.randomUUID().toString())
                        .text("comment 1").build()))
                .build());

        Comment comment = Comment.builder()
                .text("commentNew")
                .build();
        commentRepository.save(book.getId(), comment);

        Book foundBook = mongoTemplate.findOne(new Query(Criteria
                .where("_id").is(book.getId())
                .and("comments.text").is(comment.getText())), Book.class);
        assert foundBook != null;

        assertThat(foundBook.getComments())
                .contains(comment);
    }

    @DisplayName("Обновить комментарий в книге")
    @Test
    void updateTest() {
        Comment comment = Comment.builder()
                .id(UUID.randomUUID().toString())
                .text("comment 1").build();

        Book book = mongoTemplate.insert(Book.builder()
                .name("Test book 2")
                .comments(List.of(comment))
                .build());

        comment.setText("newComment");

        commentRepository.save(book.getId(), comment);

        Book foundBook = mongoTemplate.findOne(new Query(Criteria
                .where("_id").is(book.getId())
                .and("comments._id").is(comment.getId())), Book.class);
        assert foundBook != null;

        assertThat(foundBook.getComments())
            .containsOnly(comment);
    }

    @DisplayName("Получить все комментарии по книге")
    @Test
    void findByBookIdTest() {
        final Comment comment = Comment.builder()
                .id(UUID.randomUUID().toString())
                .text("Comment1").build();

        final Book book = Book.builder()
                .name("Test book 1")
                .comments(List.of(comment))
                .build();

        mongoTemplate.insert(book);

        Page<Comment> page = commentRepository.findByBookId(book.getId(), PageRequest.of(0, 10));

        assertThat(page.getContent().stream().findFirst())
                .get()
                .extracting(Comment::getText)
                .isEqualTo(comment.getText());
    }

    @DisplayName("Получить комментарий по идентификатору")
    @Test
    void findByIdTest() {
        final Comment expectedComment = Comment.builder()
                .id(UUID.randomUUID().toString())
                .text("Comment1").build();

        Book book = Book.builder()
                .name("Test book 1")
                .comments(List.of(expectedComment))
                .build();

        mongoTemplate.insert(book);

        final Optional<Comment> comment = commentRepository.findById(expectedComment.getId());

        assertThat(comment)
                .get()
                .isEqualTo(expectedComment);
    }

    @DisplayName("Удалить комментарий")
    @Test
    void deleteByIdTest() {
        final Comment comment = Comment.builder()
                .id(UUID.randomUUID().toString())
                .text("Comment1").build();

        Book book = Book.builder()
                .name("Test book 1")
                .comments(List.of(comment))
                .build();

        mongoTemplate.insert(book);
        commentRepository.deleteById(comment.getId());

        assertThat(mongoTemplate.find(new Query(Criteria.where("comments._id").is(comment.getId())), Book.class))
                .isEmpty();
    }
}
