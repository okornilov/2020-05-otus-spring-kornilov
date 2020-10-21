package ru.otus.library.service;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.library.domain.Comment;

import java.util.Optional;

public interface CommentService {
    void create(@NonNull String bookId, Comment comment);
    void update(@NonNull String id, Comment comment);
    Page<Comment> findByBookId(String bookId, Pageable pageable);
    void deleteById(@NonNull String id);
}
