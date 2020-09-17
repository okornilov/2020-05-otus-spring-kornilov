package ru.otus.okornilov.homework13.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.okornilov.homework13.domain.Book;
import ru.otus.okornilov.homework13.repository.BookRepository;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public Book create(@NonNull Book book) {
        book.setId(null);
        return bookRepository.save(book);
    }

    @Override
    public Book update(@NonNull Long id, @NonNull Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Iterable<Book> findList() {
        return bookRepository.findAll();
    }

    @Override
    public void delete(@NonNull Long id) {
        bookRepository.deleteById(id);
    }
}
