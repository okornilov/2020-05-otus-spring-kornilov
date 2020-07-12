package ru.otus.library.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.library.domain.Comment;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"book"})
    @Override
    Iterable<Comment> findAll();
}
