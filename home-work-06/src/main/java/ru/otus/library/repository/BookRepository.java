package ru.otus.library.repository;

import ru.otus.library.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Book save(Book book);
    List<Book> findAll();
    Optional<Book> findById(long id);
    void deleteById(long id);
    void deleteAll();
}
