package ru.otus.library.repository;

import ru.otus.library.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {
    Author save(Author author);
    List<Author> findAll();
    Optional<Author> findById(long id);
    void deleteById(long id);
    void deleteAll();
}
