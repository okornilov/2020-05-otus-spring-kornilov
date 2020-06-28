package ru.otus.library.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;
import ru.otus.library.service.BookService;

@RequiredArgsConstructor
@ShellComponent
public class BookCommands {

    private final BookService bookService;

    @ShellMethod(value = "Book list", key = {"book-list", "books", "b"})
    public void bookList() {
        bookService.showTable();
    }

    @ShellMethod(value = "Add book", key = {"add-book", "ab"})
    public void addBook(String name, String author, String genre) {
        bookService.save(Book.builder()
                .name(name)
                .author(Author.builder().fio(author).build())
                .genre(Genre.builder().name(genre).build())
                .build());
    }

    @ShellMethod(value = "Update book", key = {"update-book", "ub"})
    public void updateBook(long id, String name, String author, String genre) {
        bookService.save(Book.builder()
                .id(id)
                .name(name)
                .author(Author.builder().fio(author).build())
                .genre(Genre.builder().name(genre).build())
                .build());
    }

    @ShellMethod(value = "Delete book", key = {"delete-book", "db"})
    public void delete(long id) {
        bookService.deleteById(id);
    }

    @ShellMethod(value = "Delete all books", key = {"delete-all-books","dab"})
    public void deleteAll() {
        bookService.deleteAll();
    }
}
