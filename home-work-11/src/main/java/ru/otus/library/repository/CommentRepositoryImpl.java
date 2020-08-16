package ru.otus.library.repository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import ru.otus.library.domain.Comment;
import ru.otus.library.dto.Page;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Repository
public class CommentRepositoryImpl implements CommentRepository {
    private final BookArrayOperations arrayOperations;
    private static final String ARRAY_NAME = "comments";

    @Override
    public Mono<Comment> create(@NonNull String bookId, Comment comment) {
        if (comment.getDateTime() == null) {
            comment.setDateTime(LocalDateTime.now().withNano(0));
        }
        return arrayOperations.addElement(bookId, comment, ARRAY_NAME);
    }

    @Override
    public  Mono<Comment> update(@NonNull String id, Comment comment) {
        comment.setId(id);
        return arrayOperations.updateElement(id, comment, ARRAY_NAME);
    }

    @Override
    public Mono<Page<Comment>> findByBookId(@NonNull String bookId, Pageable pageable) {
        return arrayOperations.findElements(ARRAY_NAME, bookId, pageable, Comment.class);
    }

    @Override
    public Mono<Void> deleteById(@NonNull String id) {
        return arrayOperations.deleteElement(id, ARRAY_NAME);
    }

}
