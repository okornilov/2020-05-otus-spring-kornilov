package ru.otus.library.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.otus.library.domain.Book;
import ru.otus.library.repository.BookRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Optional<Book> findById(@NonNull String id) {
        return bookRepository.findById(id);
    }

    @Override
    public Page<Book> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @Override
    public void deleteById(@NonNull String id) {
        bookRepository.deleteById(id);
    }
}
