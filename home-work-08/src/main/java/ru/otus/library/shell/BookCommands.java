package ru.otus.library.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.library.domain.Book;
import ru.otus.library.service.BookService;
import ru.otus.library.transformer.BookTransformer;

@RequiredArgsConstructor
@ShellComponent
public class BookCommands {

    private final BookService bookService;
    private final BookTransformer bookTransformer;

    @ShellMethod(value = "Book list", key = {"book-list", "books", "b"})
    public void bookList(@ShellOption(defaultValue = "0") int page, @ShellOption(defaultValue = "10") int size) {
        bookService.showTable(PageRequest.of(page, size));
    }

    @ShellMethod(value = "Add book", key = {"add-book", "ab"})
    public void addBook(String name, String[] authors, String[] genres) {
        Book book = bookTransformer.transform(name, authors, genres);
        bookService.save(book);
    }

    @ShellMethod(value = "Update book", key = {"update-book", "ub"})
    public void updateBook(String id, String name, String[] authors, String[] genres) {
        Book book = bookTransformer.transform(id, name, authors, genres);
        bookService.save(book);
    }

    @ShellMethod(value = "Delete book", key = {"delete-book", "db"})
    public void delete(String id) {
        bookService.deleteById(id);
    }

    @ShellMethod(value = "Delete all books", key = {"delete-all-books","dab"})
    public void deleteAll() {
        bookService.deleteAll();
    }
}
