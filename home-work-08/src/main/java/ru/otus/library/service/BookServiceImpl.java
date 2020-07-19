package ru.otus.library.service;

import de.vandermeer.asciitable.AsciiTable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;
import ru.otus.library.repository.BookRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final IOService ioService;
    private final MessageBundleService bundleService;

    @Override
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Override
    public void showTable(Pageable pageable) {
        ioService.outLine(bundleService.getMessage("book.list") + ":");
        AsciiTable asciiTable = new AsciiTable();
        asciiTable.addRule();
        asciiTable.addRow(bundleService.getMessage("book.id"), bundleService.getMessage("book.name"),
                bundleService.getMessage("authors"), bundleService.getMessage("genres"));

        Page<Book> page = bookRepository.findAll(pageable);
        page.forEach(book -> {
            List<Author> authors = Optional.ofNullable(book.getAuthors()).orElse(new LinkedList<>());
            List<Genre> genres = Optional.ofNullable(book.getGenres()).orElse(new LinkedList<>());
            asciiTable.addRule();
            asciiTable.addRow(book.getId(), book.getName(),
                    authors.stream().map(Author::getFullName).collect(Collectors.joining(", ")),
                    genres.stream().map(Genre::getName).collect(Collectors.joining(", ")));
        });

        asciiTable.addRule();
        ioService.outLine(asciiTable.render());
        ioService.outLine(bundleService.getMessage("page", page.getNumber() + 1, page.getTotalPages()));
        ioService.outLine(bundleService.getMessage("records", page.getTotalElements()));
    }

    @Override
    public void deleteById(@NonNull String id) {
        bookRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        bookRepository.deleteAll();
    }
}
