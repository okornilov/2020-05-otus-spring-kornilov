package ru.otus.library.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.library.domain.Author;
import ru.otus.library.service.AuthorService;

@RequiredArgsConstructor
@ShellComponent
public class AuthorCommands {
    private final AuthorService authorService;

    @ShellMethod(value = "Add author", key = {"add-author", "aa"})
    public void addAuthor(String bookId, String fullName) {
        authorService.create(bookId, Author.builder()
                .fullName(fullName)
                .build());
    }
    @ShellMethod(value = "Update author", key = {"update-author", "ua"})
    public void updateAuthor(String id, String fullName) {
        authorService.update(id, Author.builder()
                .fullName(fullName)
                .build());
    }

    @ShellMethod(value = "All author list", key = {"all-author-list", "all-authors", "all-a"})
    public void authorList(@ShellOption(defaultValue = "0") int page, @ShellOption(defaultValue = "10") int size) {
        authorService.showTable(PageRequest.of(page, size));
    }

    @ShellMethod(value = "Book author list", key = {"author-list", "authors", "a"})
    public void authorList(String bookId, @ShellOption(defaultValue = "0") int page, @ShellOption(defaultValue = "10") int size) {
        authorService.showTable(bookId, PageRequest.of(page, size));
    }

    @ShellMethod(value = "Delete author", key = {"delete-author", "da"})
    public void delete(String id) {
        authorService.deleteById(id);
    }

    @ShellMethod(value = "Delete all authors", key = {"delete-all-authors","daa"})
    public void deleteAll() {
        authorService.deleteAll();
    }
}
