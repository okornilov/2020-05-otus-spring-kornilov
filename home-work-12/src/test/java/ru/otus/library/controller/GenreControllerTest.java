package ru.otus.library.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.library.domain.Genre;
import ru.otus.library.service.GenreService;

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

@DisplayName("Тестирование GenreController")
@WebMvcTest(GenreController.class)
@WithMockUser(username = "user")
class GenreControllerTest {

    @Import(GenreController.class)
    @Configuration
    static class TestConfig{}

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenreService genreService;

    @Test
    void genreListTest() throws Exception {
        final String bookId = "1";
        final int page = 1;
        final int size = 10;
        final Genre genre = Genre.builder()
                .id(UUID.randomUUID().toString())
                .name("genre")
                .build();
        final PageRequest pageable = PageRequest.of(page - 1, size);

        when(genreService.findByBookId(bookId, pageable))
                .thenReturn(new PageImpl<>(List.of(genre), pageable, 1));

        mockMvc.perform(get("/genres?bookId=" + bookId))
            .andExpect(status().isOk())
            .andExpect(view().name("genre-list"))
            .andExpect(content().string(containsString(genre.getName())))
            .andExpect(content().string(containsString(genre.getId())));

        verify(genreService).findByBookId(bookId, pageable);
    }

    @Test
    void genreGetTest() throws Exception {
        final String bookId = "1";
        final String genreId = "1";

        final Genre genre = Genre.builder()
                .id(genreId)
                .name("genre1")
                .build();

        when(genreService.findById(genreId))
                .thenReturn(Optional.of(genre));

        mockMvc.perform(get(String.format("/genre?bookId=%s&id=%s", bookId, genreId)))
                .andExpect(status().isOk())
                .andExpect(view().name("genre-edit"))
                .andExpect(content().string(containsString(genre.getName())));

        verify(genreService).findById(genreId);
    }

    @Test
    void genreSave() throws Exception {
        final String bookId = "1";
        final Genre genre = Genre.builder()
                .id(UUID.randomUUID().toString())
                .name("genre1")
                .build();

        mockMvc.perform(post("/genre")
                    .with(csrf())
                    .param("bookId", bookId)
                    .flashAttr("genre", genre))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/genres?bookId=" + bookId));

        verify(genreService).save(bookId, genre);
    }

    @Test
    void genreDeleteTest() throws Exception {
        final String bookId = "1";
        final String genreId = "2";

        mockMvc.perform(post("/genre-delete")
                    .with(csrf())
                    .param("bookId", bookId)
                    .param("id", genreId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/genres?bookId=" + bookId));

        verify(genreService).deleteById(genreId);
    }
}