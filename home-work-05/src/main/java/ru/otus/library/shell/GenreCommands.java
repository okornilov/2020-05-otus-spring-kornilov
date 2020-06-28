package ru.otus.library.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.library.domain.Genre;
import ru.otus.library.service.GenreService;

@RequiredArgsConstructor
@ShellComponent
public class GenreCommands {

    private final GenreService genreService;

    @ShellMethod(value = "Genre list", key = {"genre-list", "genres", "g"})
    public void genreList() {
        genreService.showTable();
    }

    @ShellMethod(value = "Add genre", key = {"add-genre", "ag"})
    public void addGenre(String name) {
        genreService.save(Genre.builder()
                .name(name)
                .build());
    }

    @ShellMethod(value = "Update genre", key = {"update-genre", "ug"})
    public void updateGenre(long id, String name) {
        genreService.save(Genre.builder()
                .id(id)
                .name(name)
                .build());
    }

    @ShellMethod(value = "Delete genre", key = {"delete-genre", "dg"})
    public void delete(long id) {
        genreService.deleteById(id);
    }

    @ShellMethod(value = "Delete all genres", key = {"delete-all-genres","dag"})
    public void deleteAll() {
        genreService.deleteAll();
    }
}
