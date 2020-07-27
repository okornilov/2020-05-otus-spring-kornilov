package ru.otus.library.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.library.domain.Genre;
import ru.otus.library.dto.GenreDto;

public interface GenreRepository {
    void add(@NonNull String bookId, Genre genre);
    void update(@NonNull String id, Genre genre);
    Page<GenreDto> findByBookId(@NonNull String bookId, Pageable pageable);
    Page<GenreDto> findAll(Pageable pageable);
    void deleteById(@NonNull String id);
    void deleteAll();
}
