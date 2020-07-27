package ru.otus.library.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.library.domain.Genre;
import ru.otus.library.service.GenreService;

@RequiredArgsConstructor
@ShellComponent
public class GenreCommands {
    private final GenreService genreService;

    @ShellMethod(value = "Add genre", key = {"add-genre", "ag"})
    public void addGenre(String bookId, String name) {
        genreService.create(bookId, Genre.builder()
                .name(name)
                .build());
    }
    @ShellMethod(value = "Update genre", key = {"update-genre", "ug"})
    public void updateGenre(String id, String name) {
        genreService.update(id, Genre.builder()
                .name(name)
                .build());
    }

    @ShellMethod(value = "All genre list", key = {"all-genre-list", "all-genres", "all-g"})
    public void genreList(@ShellOption(defaultValue = "0") int page, @ShellOption(defaultValue = "10") int size) {
        genreService.showTable(PageRequest.of(page, size));
    }

    @ShellMethod(value = "Book genre list", key = {"genre-list", "genres", "g"})
    public void genreList(String bookId, @ShellOption(defaultValue = "0") int page, @ShellOption(defaultValue = "10") int size) {
        genreService.showTable(bookId, PageRequest.of(page, size));
    }

    @ShellMethod(value = "Delete genre", key = {"delete-genre", "dg"})
    public void delete(String id) {
        genreService.deleteById(id);
    }

    @ShellMethod(value = "Delete all genres", key = {"delete-all-genres","dag"})
    public void deleteAll() {
        genreService.deleteAll();
    }
}
