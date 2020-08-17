package ru.otus.library.repository;

import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;
import ru.otus.library.domain.Comment;
import ru.otus.library.dto.Page;

public interface CommentRepository {
    Mono<Comment> create(@NonNull String bookId, Comment comment);
    Mono<Comment> update(@NonNull String id, Comment comment);
    Mono<Page<Comment>> findByBookId(@NonNull String bookId, Pageable pageable);
    Mono<Void> deleteById(@NonNull String id);
}
