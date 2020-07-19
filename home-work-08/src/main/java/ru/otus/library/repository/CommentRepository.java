package ru.otus.library.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.library.domain.Comment;
import ru.otus.library.dto.CommentDto;

public interface CommentRepository {
    void add(@NonNull String bookId, Comment comment);
    void update(@NonNull String id, Comment comment);
    Page<CommentDto> findAll(Pageable pageable);
    Page<CommentDto> findByBookId(@NonNull String bookId, Pageable pageable);
    void deleteById(@NonNull String id);
    void deleteAll();
}
