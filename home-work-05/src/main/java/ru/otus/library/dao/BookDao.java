package ru.otus.library.dao;

import ru.otus.library.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookDao {
    Book save(Book book);
    List<Book> findAll();
    Optional<Book> findByName(String name);
    Optional<Book> findById(long id);
    void deleteById(long id);
    void deleteAll();
}
