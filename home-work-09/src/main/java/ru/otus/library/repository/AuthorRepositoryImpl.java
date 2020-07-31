package ru.otus.library.repository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import ru.otus.library.domain.Author;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class AuthorRepositoryImpl implements AuthorRepository {
    private final BookArrayOperations arrayOperations;
    private static final String ARRAY_NAME = "authors";

    @Override
    public void save(@NonNull String bookId, Author author) {
        arrayOperations.saveElement(bookId, author, ARRAY_NAME);
    }

    @Override
    public Optional<Author> findById(String id) {
        return Optional.ofNullable(arrayOperations.findById(ARRAY_NAME, id, Author.class));
    }

    @Override
    public Page<Author> findAllByBookId(@NonNull String bookId, Pageable pageable) {
        return arrayOperations.findElements(ARRAY_NAME, bookId, pageable, Author.class);
    }

    @Override
    public void deleteById(@NonNull String id) {
        arrayOperations.deleteElement(id, ARRAY_NAME);
    }
}
