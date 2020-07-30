package ru.otus.library.service;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.library.domain.Author;

import java.util.Optional;

public interface AuthorService {
    void save(@NonNull String bookId, Author author);
    Optional<Author> findById(String id);
    Page<Author> findByBookId(String bookId, Pageable pageable);
    void deleteById(@NonNull String id);
}
