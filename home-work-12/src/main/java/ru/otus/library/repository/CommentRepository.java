package ru.otus.library.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.library.domain.Comment;

import java.util.Optional;

public interface CommentRepository {
    void save(@NonNull String bookId, Comment comment);
    Optional<Comment> findById(@NonNull String id);
    Page<Comment> findByBookId(@NonNull String bookId, Pageable pageable);
    void deleteById(@NonNull String id);
}
