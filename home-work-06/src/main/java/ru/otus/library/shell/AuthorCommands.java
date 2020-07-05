package ru.otus.library.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.library.domain.Author;
import ru.otus.library.service.AuthorService;

@RequiredArgsConstructor
@ShellComponent
public class AuthorCommands {

    private final AuthorService authorService;

    @ShellMethod(value = "Author list", key = {"author-list", "authors", "a"})
    public void authorList() {
        authorService.showTable();
    }

    @ShellMethod(value = "Author genre", key = {"add-author", "aa"})
    public void addAuthor(String fullName) {
        authorService.save(Author.builder()
                .fullName(fullName)
                .build());
    }

    @ShellMethod(value = "Update author", key = {"update-author", "ua"})
    public void updateAuthor(long id, String fullName) {
        authorService.save(Author.builder()
                .id(id)
                .fullName(fullName)
                .build());
    }

    @ShellMethod(value = "Delete author", key = {"delete-author", "da"})
    public void delete(long id) {
        authorService.deleteById(id);
    }

    @ShellMethod(value = "Delete all authors", key = {"delete-all-authors","daa"})
    public void deleteAll() {
        authorService.deleteAll();
    }
}
