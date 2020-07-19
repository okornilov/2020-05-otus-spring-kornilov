package ru.otus.library.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.library.domain.Author;
import ru.otus.library.dto.AuthorDto;

public interface AuthorRepository {
    void add(@NonNull String bookId, Author author);
    void update(@NonNull String id, Author author);
    Page<AuthorDto> findAll(Pageable pageable);
    Page<AuthorDto> findByBookId(@NonNull String bookId, Pageable pageable);
    void deleteById(@NonNull String id);
    void deleteAll();
}
