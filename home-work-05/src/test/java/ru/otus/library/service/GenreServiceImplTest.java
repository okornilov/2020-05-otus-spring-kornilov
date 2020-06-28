package ru.otus.library.service;

import de.vandermeer.asciitable.AsciiTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.library.dao.GenreDao;
import ru.otus.library.domain.Genre;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Тестирование GenreServiceImpl")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = GenreServiceImpl.class)
class GenreServiceImplTest {

    @Autowired
    private GenreService genreService;

    @MockBean
    private IOService ioService;

    @MockBean
    private GenreDao genreDao;

    @MockBean
    private MessageBundleService bundleService;

    @DisplayName("Сохранить жанр")
    @Test
    void save() {
        Genre genre = Genre.builder().id(1L).name("Genre").build();
        genreService.save(genre);
        verify(genreDao).save(genre);
    }

    @DisplayName("Показать таблицу жанров")
    @Test
    void showTable() {
        when(bundleService.getMessage("genre.list")).thenReturn("genre list");
        when(bundleService.getMessage("genre.id")).thenReturn("genre_id");
        when(bundleService.getMessage("genre.name")).thenReturn("genre_name");
        List<Genre> genreList = List.of(
                Genre.builder().id(1L).name("Genre1").build(),
                Genre.builder().id(2L).name("Genre2").build());
        when(genreDao.findAll()).thenReturn(genreList);

        AsciiTable asciiTable = new AsciiTable();
        asciiTable.addRule();
        asciiTable.addRow(List.of("genre_id", "genre_name"));
        genreList.forEach(genre -> {
            asciiTable.addRule();
            asciiTable.addRow(genre.getId(), genre.getName());
        });
        asciiTable.addRule();

        genreService.showTable();
        verify(ioService).outLine("genre list:");
        verify(ioService).outLine(asciiTable.render());
    }

    @DisplayName("Удалить жанр по идентификатору")
    @Test
    void deleteById() {
        genreService.deleteById(1L);
        verify(genreDao).deleteById(1L);
    }

    @DisplayName("Удалить все жанры")
    @Test
    void deleteAll() {
        genreService.deleteAll();
        verify(genreDao).deleteAll();
    }
}