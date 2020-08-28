package ru.otus.library.service;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.library.domain.Comment;

import java.util.Optional;

public interface CommentService {
    void save(@NonNull String bookId, Comment comment);
    Optional<Comment> findById(String id);
    Page<Comment> findByBookId(String bookId, Pageable pageable);
    void deleteById(@NonNull String id);
}
