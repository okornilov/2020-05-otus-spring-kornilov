package ru.otus.library.repository;

import ru.otus.library.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {
    Genre save(Genre genre);
    List<Genre> findAll();
    Optional<Genre> findById(long id);
    void deleteById(long id);
    void deleteAll();
}
