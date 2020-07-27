package ru.otus.library.service;

import de.vandermeer.asciitable.AsciiTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.library.domain.Genre;
import ru.otus.library.dto.GenreDto;
import ru.otus.library.repository.GenreRepository;

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
    private GenreRepository genreRepository;

    @MockBean
    private MessageBundleService bundleService;

    @DisplayName("Сохранить жанр")
    @Test
    void createTest() {
        String bookId = "1";
        Genre genre = Genre.builder().name("Genre").build();
        genreService.create(bookId, genre);
        verify(genreRepository).add(bookId, genre);
    }

    @DisplayName("Изменить жанр")
    @Test
    void updateTest() {
        Genre genre = Genre.builder().id("1").name("Genre").build();
        genreService.update(genre.getId(), genre);
        verify(genreRepository).update(genre.getId(), genre);
    }

    @DisplayName("Показать таблицу жанров")
    @Test
    void showTable() {
        when(bundleService.getMessage("genre.list")).thenReturn("genre list");
        when(bundleService.getMessage("genre.id")).thenReturn("genre_id");
        when(bundleService.getMessage("book.id")).thenReturn("book_id");
        when(bundleService.getMessage("book.name")).thenReturn("book_name");
        when(bundleService.getMessage("genre.name")).thenReturn("genre_name");
        List<GenreDto> genreList = List.of(
                GenreDto.builder()
                        .id("1")
                        .bookId("b1")
                        .bookName("bName1")
                        .name("Genre1").build(),
                GenreDto.builder()
                        .id("2")
                        .bookId("b2")
                        .bookName("bName2")
                        .name("Genre2").build());

        final PageRequest pageRequest = PageRequest.of(0, genreList.size());
        final PageImpl<GenreDto> page = new PageImpl<>(genreList, pageRequest, genreList.size());

        when(genreRepository.findAll(pageRequest))
                .thenReturn(page);

        AsciiTable asciiTable = new AsciiTable();
        asciiTable.addRule();
        asciiTable.addRow(List.of("genre_id", "book_id", "book_name", "genre_name"));
        genreList.forEach(g -> {
            asciiTable.addRule();
            asciiTable.addRow(g.getId(), g.getBookId(), g.getBookName(), g.getName());
        });
        asciiTable.addRule();

        genreService.showTable(pageRequest);
        verify(ioService).outLine("genre list:");
        verify(ioService).outLine(asciiTable.render());
    }

    @DisplayName("Удалить жанр по идентификатору")
    @Test
    void deleteById() {
        genreService.deleteById("1");
        verify(genreRepository).deleteById("1");
    }

    @DisplayName("Удалить все жанры")
    @Test
    void deleteAll() {
        genreService.deleteAll();
        verify(genreRepository).deleteAll();
    }
}