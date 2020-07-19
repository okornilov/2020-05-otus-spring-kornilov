package ru.otus.library.service;

import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import ru.otus.library.domain.Book;

public interface BookService {
    void save(Book book);
    void showTable(Pageable pageable);
    void deleteById(@NonNull String id);
    void deleteAll();
}
