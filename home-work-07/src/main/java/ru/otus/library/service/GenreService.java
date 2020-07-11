package ru.otus.library.service;

import ru.otus.library.domain.Genre;

public interface GenreService {
    void save(Genre genre);
    void showTable();
    void deleteById(long id);
    void deleteAll();
}
