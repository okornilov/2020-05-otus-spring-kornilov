package ru.otus.library.repository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.otus.library.domain.Genre;
import ru.otus.library.dto.GenreDto;

@RequiredArgsConstructor
@Repository
public class GenreRepositoryImpl implements GenreRepository {
    private final BookRepository bookRepository;
    private static final String ARRAY_NAME = "genres";

    @Override
    public void add(@NonNull String bookId, Genre genre) {
        bookRepository.addArrayElement(bookId, genre, ARRAY_NAME);
    }

    @Override
    public void update(@NonNull String id, Genre genre) {
        bookRepository.updateArrayElement(id, genre, ARRAY_NAME);
    }

    @Override
    public Page<GenreDto> findAll(Pageable pageable) {
        return bookRepository.findElementsByArrayName(ARRAY_NAME,null, pageable, GenreDto.class);
    }

    @Override
    public Page<GenreDto> findByBookId(@NonNull String bookId, Pageable pageable) {
        return bookRepository.findElementsByArrayName(ARRAY_NAME, bookId, pageable, GenreDto.class);
    }

    @Override
    public void deleteById(@NonNull String id) {
        bookRepository.deleteElementInArray(id, ARRAY_NAME);
    }

    @Override
    public void deleteAll() {
        bookRepository.deleteAllElementsInArray(ARRAY_NAME);
    }
}
