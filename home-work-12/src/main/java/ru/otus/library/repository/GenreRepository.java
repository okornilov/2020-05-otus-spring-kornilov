package ru.otus.library.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.library.domain.Genre;

import java.util.Optional;

public interface GenreRepository {
    void save(@NonNull String bookId, Genre genre);
    Optional<Genre> findById(@NonNull String id);
    Page<Genre> findByBookId(@NonNull String bookId, Pageable pageable);
    void deleteById(@NonNull String id);
}
