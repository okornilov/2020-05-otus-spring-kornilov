package ru.otus.library.repository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.otus.library.domain.Author;
import ru.otus.library.dto.AuthorDto;

@RequiredArgsConstructor
@Repository
public class AuthorRepositoryImpl implements AuthorRepository {
    private final BookArrayOperations arrayOperations;
    private static final String ARRAY_NAME = "authors";

    @Override
    public void add(@NonNull String bookId, Author author) {
        arrayOperations.addArrayElement(bookId, author, ARRAY_NAME);
    }

    @Override
    public void update(@NonNull String id, Author author) {
        arrayOperations.updateArrayElement(id, author, ARRAY_NAME);
    }

    @Override
    public Page<AuthorDto> findAll(Pageable pageable) {
        return arrayOperations.findElementsByArrayName(ARRAY_NAME,null, pageable, AuthorDto.class);
    }

    @Override
    public Page<AuthorDto> findByBookId(@NonNull String bookId, Pageable pageable) {
        return arrayOperations.findElementsByArrayName(ARRAY_NAME, bookId, pageable, AuthorDto.class);
    }

    @Override
    public void deleteById(@NonNull String id) {
        arrayOperations.deleteElementInArray(id, ARRAY_NAME);
    }

    @Override
    public void deleteAll() {
        arrayOperations.deleteAllElementsInArray(ARRAY_NAME);
    }
}
