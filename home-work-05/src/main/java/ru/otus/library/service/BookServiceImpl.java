package ru.otus.library.service;

import de.vandermeer.asciitable.AsciiTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.library.dao.BookDao;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;
    private final IOService ioService;
    private final MessageBundleService bundleService;

    @Override
    public void save(Book book) {
        bookDao.save(book);
    }

    @Override
    public void showTable() {
        ioService.outLine(bundleService.getMessage("book.list") + ":");
        AsciiTable asciiTable = new AsciiTable();
        asciiTable.addRule();

        asciiTable.addRow(
                bundleService.getMessage("book.id"),
                bundleService.getMessage("book.name"),
                bundleService.getMessage("author.id"),
                bundleService.getMessage("author.fio"),
                bundleService.getMessage("genre.id"),
                bundleService.getMessage("genre.name"));

        bookDao.findAll().forEach(book -> {
            Author author = Optional.ofNullable(book.getAuthor()).orElse(new Author());
            Genre genre = Optional.ofNullable(book.getGenre()).orElse(new Genre());
            asciiTable.addRule();
            asciiTable.addRow(book.getId(),
                    book.getName(), author.getId(), author.getFio(),
                    genre.getId(), genre.getName());
        });

        asciiTable.addRule();
        ioService.outLine(asciiTable.render());
    }

    @Override
    public void deleteById(long id) {
        bookDao.deleteById(id);
    }

    @Override
    public void deleteAll() {
        bookDao.deleteAll();
    }
}
