package ru.otus.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;
import ru.otus.library.mapper.BookMapper;
import ru.otus.library.service.BookService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Тестирование BookController")
@WebMvcTest(BookController.class)
class BookControllerTest {

    @Import(BookController.class)
    @ComponentScan(basePackageClasses = BookMapper.class)
    @Configuration
    static class TestConfig{}

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    @DisplayName("Получить список книг")
    @Test
    void bookListTest() throws Exception {
        final Author author = Author.builder()
                .id(UUID.randomUUID().toString())
                .fullName("Author1")
                .build();

        final Genre genre = Genre.builder()
                .id(UUID.randomUUID().toString())
                .name("Genre")
                .build();

        final Book book = Book.builder()
                .id(UUID.randomUUID().toString())
                .name("book")
                .authors(List.of(author))
                .genres(List.of(genre))
                .build();

        when(bookService.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(book), PageRequest.of(0, 1), 1));

        mockMvc.perform(get("/api/books")
                .content(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content[0].name").value(book.getName()))
            .andExpect(jsonPath("$.content[0].id").value(book.getId()))
            .andExpect(jsonPath("$.content[0].authors").isArray())
            .andExpect(jsonPath("$.content[0].authors[0].id").value(author.getId()))
            .andExpect(jsonPath("$.content[0].authors[0].fullName").value(author.getFullName()))
            .andExpect(jsonPath("$.content[0].genres").isArray())
            .andExpect(jsonPath("$.content[0].genres[0].id").value(genre.getId()))
            .andExpect(jsonPath("$.content[0].genres[0].name").value(genre.getName()));

        verify(bookService).findAll(any(Pageable.class));
    }

    @DisplayName("Получить книгу по идентификатору")
    @Test
    void bookGetTest() throws Exception {
        final String bookId = "1";
        final Author author = Author.builder()
                .id(UUID.randomUUID().toString())
                .fullName("Author1")
                .build();
        final Genre genre = Genre.builder()
                .id(UUID.randomUUID().toString())
                .name("Genre")
                .build();
        final Book book = Book.builder()
                .id(bookId)
                .name("book1")
                .authors(List.of(author))
                .genres(List.of(genre))
                .build();

        when(bookService.findById(bookId))
                .thenReturn(Optional.of(book));

        mockMvc.perform(get("/api/book/" + bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(book.getName())))
                .andExpect(jsonPath("$.id", is(book.getId())))
                .andExpect(jsonPath("$.authors").isArray())
                .andExpect(jsonPath("$.authors[0].id").value(author.getId()))
                .andExpect(jsonPath("$.authors[0].fullName").value(author.getFullName()))
                .andExpect(jsonPath("$.genres").isArray())
                .andExpect(jsonPath("$.genres[0].id").value(genre.getId()))
                .andExpect(jsonPath("$.genres[0].name").value(genre.getName()));

        verify(bookService).findById(bookId);
    }

    @DisplayName("Создать книгу")
    @Test
    void bookCreateTest() throws Exception {
        final Book book = Book.builder()
                .id(UUID.randomUUID().toString())
                .name("book1")
                .build();

        given(bookService.save(book))
                .will(invocationOnMock -> invocationOnMock.getArgument(0));

        mockMvc.perform(post("/api/book")
                    .content(objectMapper.writeValueAsString(book))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(book.getName())))
                .andExpect(jsonPath("$.id", is(book.getId())));

        verify(bookService).save(book);
    }

    @DisplayName("Обновить книгу")
    @Test
    void bookUpdateTest() throws Exception {
        final Book book = Book.builder()
                .id(UUID.randomUUID().toString())
                .name("book1")
                .build();

        given(bookService.save(book))
                .will(invocationOnMock -> invocationOnMock.getArgument(0));

        mockMvc.perform(put("/api/book/" + book.getId())
                    .content(objectMapper.writeValueAsString(book))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(book.getName())))
                .andExpect(jsonPath("$.id", is(book.getId())));

        verify(bookService).save(book);
    }

    @DisplayName("Удалить книгу")
    @Test
    void bookDeleteTest() throws Exception {
        final String bookId = "1";

        mockMvc.perform(delete("/api/book/" + bookId))
                .andExpect(status().isOk());

        verify(bookService).deleteById(bookId);
    }
}