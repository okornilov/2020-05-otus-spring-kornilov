package ru.otus.library.dao;

import ru.otus.library.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorDao {
    Author save(Author author);
    List<Author> findAll();
    Optional<Author> findById(long id);
    Optional<Author> findByFio(String fio);
    void deleteById(long id);
    void deleteAll();
}
