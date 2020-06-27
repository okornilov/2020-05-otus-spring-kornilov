package ru.otus.library.dao;

import ru.otus.library.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDao {
    Genre save(Genre genre);
    List<Genre> findAll();
    Optional<Genre> findById(long id);
    Optional<Genre> findByName(String name);
    void deleteById(long id);
    void deleteAll();
}
