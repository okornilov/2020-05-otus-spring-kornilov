package ru.otus.okornilov.homework13.service;

import lombok.NonNull;
import ru.otus.okornilov.homework13.domain.Book;


public interface BookService {
    Book create(@NonNull Book book);
    Book update(@NonNull Long id, @NonNull Book book);
    Iterable<Book> findList();
    void delete(@NonNull Long id);
}
