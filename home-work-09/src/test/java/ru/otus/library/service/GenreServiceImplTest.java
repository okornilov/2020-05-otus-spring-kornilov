package ru.otus.library.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.library.domain.Genre;
import ru.otus.library.repository.GenreRepository;

import static org.mockito.Mockito.verify;

@DisplayName("Тестирование GenreServiceImpl")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = GenreServiceImpl.class)
class GenreServiceImplTest {

    @Autowired
    private GenreService genreService;

    @MockBean
    private GenreRepository genreRepository;


    @DisplayName("Сохранить жанр")
    @Test
    void saveTest() {
        String bookId = "1";
        Genre genre = Genre.builder().name("Genre").build();
        genreService.save(bookId, genre);
        verify(genreRepository).save(bookId, genre);
    }

    @DisplayName("Получить жанр по идентификатору")
    @Test
    void findByIdTest() {
        String genreId = "1";
        genreService.findById(genreId);
        verify(genreRepository).findById(genreId);
    }

    @DisplayName("Получить жанры по книге")
    @Test
    void findByBookIdTest() {
        String bookId = "1";
        Pageable pageable = PageRequest.of(0, 1);
        genreService.findByBookId(bookId, pageable);
        verify(genreRepository).findByBookId(bookId, pageable);
    }

    @DisplayName("Удалить жанр по идентификатору")
    @Test
    void deleteById() {
        genreService.deleteById("1");
        verify(genreRepository).deleteById("1");
    }
}