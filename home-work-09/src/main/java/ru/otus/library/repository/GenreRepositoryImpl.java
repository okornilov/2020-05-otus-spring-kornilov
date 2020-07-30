package ru.otus.library.repository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.otus.library.domain.Genre;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class GenreRepositoryImpl implements GenreRepository {
    private final BookArrayOperations arrayOperations;
    private static final String ARRAY_NAME = "genres";

    @Override
    public void save(@NonNull String bookId, Genre genre) {
       arrayOperations.saveElement(bookId, genre, ARRAY_NAME);
    }

    @Override
    public Optional<Genre> findById(@NonNull String id) {
        return Optional.ofNullable(arrayOperations.findById(ARRAY_NAME, id, Genre.class));
    }

    @Override
    public Page<Genre> findByBookId(@NonNull String bookId, Pageable pageable) {
        return arrayOperations.findElements(ARRAY_NAME, bookId, pageable, Genre.class);
    }

    @Override
    public void deleteById(@NonNull String id) {
        arrayOperations.deleteElement(id, ARRAY_NAME);
    }
}
