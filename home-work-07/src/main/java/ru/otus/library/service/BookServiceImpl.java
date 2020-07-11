package ru.otus.library.service;

import de.vandermeer.asciitable.AsciiTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;
import ru.otus.library.repository.BookRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final IOService ioService;
    private final MessageBundleService bundleService;

    @Transactional
    @Override
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Transactional(readOnly = true)
    @Override
    public void showTable() {
        ioService.outLine(bundleService.getMessage("book.list") + ":");
        AsciiTable asciiTable = new AsciiTable();
        asciiTable.addRule();

        asciiTable.addRow(
                bundleService.getMessage("book.id"),
                bundleService.getMessage("book.name"),
                bundleService.getMessage("author.id"),
                bundleService.getMessage("author.fullName"),
                bundleService.getMessage("genre.id"),
                bundleService.getMessage("genre.name"));

        bookRepository.findAll().forEach(book -> {
            Author author = Optional.ofNullable(book.getAuthor()).orElse(new Author());
            Genre genre = Optional.ofNullable(book.getGenre()).orElse(new Genre());
            asciiTable.addRule();
            asciiTable.addRow(book.getId(),
                    book.getName(), author.getId(), author.getFullName(),
                    genre.getId(), genre.getName());
        });

        asciiTable.addRule();
        ioService.outLine(asciiTable.render());
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteAll() {
        bookRepository.deleteAll();
    }
}
