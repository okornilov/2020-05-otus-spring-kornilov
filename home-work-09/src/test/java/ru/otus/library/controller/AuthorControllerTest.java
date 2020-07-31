package ru.otus.library.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.library.domain.Author;
import ru.otus.library.service.AuthorService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Тестирование AuthorController")
@SpringBootTest
@AutoConfigureMockMvc
class AuthorControllerTest {
    @Autowired
    AuthorController authorController;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthorService authorService;

    @Test
    void authorListTest() throws Exception {
        final String bookId = "1";
        final int page = 1;
        final int size = 10;
        final Author author = Author.builder()
                .id(UUID.randomUUID().toString())
                .fullName("author")
                .build();
        final PageRequest pageable = PageRequest.of(page - 1, size);

        when(authorService.findByBookId(bookId, pageable))
                .thenReturn(new PageImpl<>(List.of(author), pageable, 1));

        mockMvc.perform(get("/authors?bookId=" + bookId))
            .andExpect(status().isOk())
            .andExpect(view().name("author-list"))
            .andExpect(content().string(containsString(author.getFullName())))
            .andExpect(content().string(containsString(author.getId())));

        verify(authorService).findByBookId(bookId, pageable);
    }

    @Test
    void authorGetTest() throws Exception {
        final String bookId = "1";
        final String authorId = "1";

        final Author author = Author.builder()
                .id(authorId)
                .fullName("author1")
                .build();

        when(authorService.findById(authorId))
                .thenReturn(Optional.of(author));

        mockMvc.perform(get(String.format("/author?bookId=%s&id=%s", bookId, authorId)))
                .andExpect(status().isOk())
                .andExpect(view().name("author-edit"))
                .andExpect(content().string(containsString(author.getFullName())));

        verify(authorService).findById(authorId);
    }

    @Test
    void authorSave() throws Exception {
        final String bookId = "1";
        final Author author = Author.builder()
                .id(UUID.randomUUID().toString())
                .fullName("author1")
                .build();

        mockMvc.perform(post("/author")
                    .param("bookId", bookId)
                    .flashAttr("author", author))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/authors?bookId=" + bookId));

        verify(authorService).save(bookId, author);
    }

    @Test
    void authorDeleteTest() throws Exception {
        final String bookId = "1";
        final String authorId = "2";

        mockMvc.perform(post("/author-delete")
                    .param("bookId", bookId)
                    .param("id", authorId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/authors?bookId=" + bookId));

        verify(authorService).deleteById(authorId);
    }
}