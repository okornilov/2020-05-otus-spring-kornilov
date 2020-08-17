package ru.otus.library.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Comment;
import ru.otus.library.dto.Page;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование CommentRepository")
@DataMongoTest
@Import(TestRepositoryConfiguration.class)
class CommentRepositoryTest {

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        reactiveMongoTemplate.dropCollection(Book.class).block();
    }

    @DisplayName("Создать комментарий")
    @Test
    void createTest() {
        final Book book = Book.builder().name("book").build();
        final Comment comment = Comment.builder().text("comment").build();

        final Mono<Comment> commentMono = reactiveMongoTemplate.save(book)
                .flatMap(b ->
                        commentRepository.create(book.getId(), comment));

        StepVerifier.create(commentMono)
                .assertNext(c -> assertThat(c).isEqualToComparingFieldByField(comment))
                .expectComplete()
                .verify();

        book.setComments(List.of(comment));

        StepVerifier.create(reactiveMongoTemplate.findOne(new Query(Criteria.where("_id").is(book.getId())
                .and("comments.text").is(comment.getText())), Book.class))
            .assertNext(b -> assertThat(b)
                    .isEqualToComparingFieldByField(book))
            .expectComplete()
            .verify();
    }

    @Test
    void updateTest() {
        final Comment comment = Comment.builder()
                .id(UUID.randomUUID().toString())
                .text("comment")
                .build();

        final Book book = Book.builder()
                .name("book")
                .comments(List.of(comment))
                .build();

        final Mono<Comment> commentMono = reactiveMongoTemplate.save(book)
                .flatMap(b ->
                        commentRepository.update(comment.getId(), comment));

        StepVerifier.create(commentMono)
                .assertNext(c -> assertThat(c).isEqualToComparingFieldByField(comment))
                .expectComplete()
                .verify();

        StepVerifier.create(reactiveMongoTemplate.findOne(new Query(Criteria.where("_id").is(book.getId())
                .and("comments.text").is(comment.getText())), Book.class))
                .assertNext(b -> assertThat(b)
                        .isEqualToComparingFieldByField(book))
                .expectComplete()
                .verify();
    }

    @Test
    void findByBookIdTest() {
        final Comment comment = Comment.builder()
                .id(UUID.randomUUID().toString())
                .text("Comment1").build();

        final Book book = Book.builder()
                .name("Test book 1")
                .comments(List.of(comment))
                .build();

        final Mono<Page<Comment>> pageMono = reactiveMongoTemplate.insert(book)
                .flatMap(b ->
                        commentRepository.findByBookId(b.getId(), PageRequest.of(0, 10)));

        StepVerifier.create(pageMono)
                .assertNext(page -> assertThat(page.getContent().stream().findFirst()).get()
                        .extracting(Comment::getText)
                        .isEqualTo(comment.getText()))
                .expectComplete()
                .verify();

    }

    @Test
    void deleteByIdTest() {
        final Comment comment = Comment.builder()
                .id(UUID.randomUUID().toString())
                .text("Comment1").build();

        Book book = Book.builder()
                .name("Test book 1")
                .comments(List.of(comment))
                .build();

        final Mono<Void> mono = reactiveMongoTemplate.insert(book)
                .flatMap(b -> commentRepository.deleteById(comment.getId()));

        StepVerifier.create(mono)
                .expectNext()
                .expectComplete()
                .verify();

        StepVerifier
                .create(reactiveMongoTemplate.findOne(new Query(Criteria.where("comments._id").is(comment.getId())), Book.class))
                .expectComplete()
                .verify();
    }
}