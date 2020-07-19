package ru.otus.library.service;

import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import ru.otus.library.domain.Genre;

public interface GenreService {
    void create(@NonNull String bookId, Genre genre);
    void update(@NonNull String id, Genre genre);
    void showTable(Pageable pageable);
    void showTable(@NonNull String bookId, Pageable pageable);
    void deleteById(@NonNull String id);
    void deleteAll();
}
