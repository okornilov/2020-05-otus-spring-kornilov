package ru.otus.library.repository;

import ru.otus.library.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Comment save(Comment comment);
    List<Comment> findAll();
    Optional<Comment> findById(long id);
    void deleteById(long id);
    void deleteAll();
}
