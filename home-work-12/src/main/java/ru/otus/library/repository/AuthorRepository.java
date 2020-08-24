package ru.otus.library.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.library.domain.Author;

import java.util.Optional;

public interface AuthorRepository {
    void save(@NonNull String bookId, Author author);
    Optional<Author> findById(String id);
    Page<Author> findAllByBookId(@NonNull String bookId, Pageable pageable);
    void deleteById(@NonNull String id);
}
