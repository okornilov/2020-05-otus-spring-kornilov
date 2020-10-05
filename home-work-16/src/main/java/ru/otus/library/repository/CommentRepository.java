package ru.otus.library.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;
import ru.otus.library.domain.Comment;

@NoRepositoryBean
public interface CommentRepository extends Repository<Comment, String> {
    void create(@NonNull String bookId, Comment comment);
    void update(@NonNull String id, Comment comment);
    Page<Comment> findAll(Pageable pageable);
    Page<Comment> findByBookId(@NonNull String bookId, Pageable pageable);
    Comment findById(String id);
    void deleteById(@NonNull String id);
}
