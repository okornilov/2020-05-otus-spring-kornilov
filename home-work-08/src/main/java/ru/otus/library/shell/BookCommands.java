package ru.otus.library.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;
import ru.otus.library.service.BookService;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@ShellComponent
public class BookCommands {

    private final BookService bookService;

    @ShellMethod(value = "Book list", key = {"book-list", "books", "b"})
    public void bookList(@ShellOption(defaultValue = "0") int page, @ShellOption(defaultValue = "10") int size) {
        bookService.showTable(PageRequest.of(page, size));
    }

    @ShellMethod(value = "Add book", key = {"add-book", "ab"})
    public void addBook(String name, String[] authors, String[] genres) {
        bookService.save(toBook(name, authors, genres, Book.builder()));
    }

    @ShellMethod(value = "Update book", key = {"update-book", "ub"})
    public void updateBook(String id, String name, String[] authors, String[] genres) {
        bookService.save(toBook(name, authors, genres, Book.builder()
                .id(id)));
    }

    private Book toBook(String name, String[] authors, String[] genres, Book.BookBuilder builder) {
        return builder
                .name(name)
                .authors(Stream.of(authors)
                        .map(s -> Author.builder()
                                .id(UUID.randomUUID().toString())
                                .fullName(s)
                                .build())
                        .collect(Collectors.toList()))
                .genres(Stream.of(genres)
                        .map(g -> Genre.builder()
                            .id(UUID.randomUUID().toString())
                            .name(g)
                            .build())
                        .collect(Collectors.toList()))
                .build();
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
