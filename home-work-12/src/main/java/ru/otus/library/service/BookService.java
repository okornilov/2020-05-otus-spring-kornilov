package ru.otus.library.service;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.library.domain.Book;
import ru.otus.library.dto.BookDto;

import java.util.Optional;

public interface BookService {
    void save(Book book);
    Optional<Book> findById(@NonNull String id);
    Page<BookDto> findAll(Pageable pageable);
    void deleteById(@NonNull String id);
}
