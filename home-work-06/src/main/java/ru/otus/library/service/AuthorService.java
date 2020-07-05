package ru.otus.library.service;

import ru.otus.library.domain.Author;

public interface AuthorService {
    void save(Author author);
    void showTable();
    void deleteById(long id);
    void deleteAll();
}
