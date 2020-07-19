package ru.otus.library.service;

import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import ru.otus.library.domain.Author;

public interface AuthorService {
    void create(@NonNull String bookId, Author author);
    void update(@NonNull String id, Author author);
    void showTable(Pageable pageable);
    void showTable(@NonNull String bookId, Pageable pageable);
    void deleteById(@NonNull String id);
    void deleteAll();
}
