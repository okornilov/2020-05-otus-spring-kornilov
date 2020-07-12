package ru.otus.library.service;

import ru.otus.library.domain.Book;

public interface BookService {
    void save(Book book);
    void showTable();
    void deleteById(long id);
    void deleteAll();
}
