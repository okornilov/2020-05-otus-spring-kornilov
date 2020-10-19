package ru.otus.library.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;
import ru.otus.library.repository.BookRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private Book stubBook = Book.builder()
            .id(UUID.randomUUID().toString())
            .name("StubBook")
            .authors(List.of(Author.builder()
                    .id(UUID.randomUUID().toString())
                    .fullName("StubAuthor")
                    .build()))
            .genres(List.of(Genre.builder()
                    .id(UUID.randomUUID().toString())
                    .name("StubGenre")
                    .build()))
            .build();

    @HystrixCommand(commandKey="books", fallbackMethod="saveFallBack")
    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @HystrixCommand(commandKey="books", fallbackMethod="findByIdFallBack")
    @Override
    public Optional<Book> findById(@NonNull String id) {
        return bookRepository.findById(id);
    }


    @HystrixCommand(commandKey="books", fallbackMethod="findAllFallBack")
    @Override
    public Page<Book> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @HystrixCommand(commandKey="books", fallbackMethod="deleteByIdFallBack")
    @Override
    public void deleteById(@NonNull String id) {
        bookRepository.deleteById(id);
    }

    public Book saveFallBack(Book book) {
        return stubBook;
    }

    private void deleteByIdFallBack(String id) {
        log.warn("Fall back on delete by id");
    }

    private Optional<Book> findByIdFallBack(String id) {
        return Optional.of(stubBook);
    }

    private Page<Book> findAllFallBack(Pageable pageable) {
        return new PageImpl<>(List.of(stubBook), pageable, 1);
    }

}
