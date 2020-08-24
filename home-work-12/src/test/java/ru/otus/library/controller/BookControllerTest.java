package ru.otus.library.controller;

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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.library.domain.Book;
import ru.otus.library.dto.BookDto;
import ru.otus.library.mapper.BookMapper;
import ru.otus.library.service.BookService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Тестирование BookController")
@WebMvcTest(BookController.class)
@WithMockUser(username = "user")
class BookControllerTest {

    @Import(BookController.class)
    @ComponentScan(basePackageClasses = BookMapper.class)
    @Configuration
    static class TestConfig{}

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    void bookListTest() throws Exception {
        final String bookId = "1";
        final int page = 1;
        final int size = 10;
        final BookDto book = BookDto.builder()
                .id(UUID.randomUUID().toString())
                .name("book")
                .build();
        final PageRequest pageable = PageRequest.of(page - 1, size);

        when(bookService.findAll(pageable))
                .thenReturn(new PageImpl<>(List.of(book), pageable, 1));

        mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(view().name("book-list"))
            .andExpect(content().string(containsString(book.getName())))
            .andExpect(content().string(containsString(book.getId())));

        verify(bookService).findAll(pageable);
    }

    @Test
    void bookGetTest() throws Exception {
        final String bookId = "1";

        final Book book = Book.builder()
                .id(bookId)
                .name("book1")
                .build();

        when(bookService.findById(bookId))
                .thenReturn(Optional.of(book));

        mockMvc.perform(get(String.format("/book?id=%s", bookId)))
                .andExpect(status().isOk())
                .andExpect(view().name("book-edit"))
                .andExpect(content().string(containsString(book.getName())));

        verify(bookService).findById(bookId);
    }

    @Test
    void bookSave() throws Exception {
        final String bookId = "1";
        final Book book = Book.builder()
                .id(UUID.randomUUID().toString())
                .name("book1")
                .build();

        mockMvc.perform(post("/book")
                    .with(csrf())
                    .param("bookId", bookId)
                    .flashAttr("book", book))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(bookService).save(book);
    }

    @Test
    void bookDeleteTest() throws Exception {
        final String bookId = "1";

        mockMvc.perform(post("/book-delete")
                    .with(csrf())
                    .param("bookId", bookId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(bookService).deleteById(bookId);
    }
}