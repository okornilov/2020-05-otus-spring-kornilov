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
import ru.otus.library.domain.Comment;
import ru.otus.library.mapper.CommentMapper;
import ru.otus.library.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Тестирование CommentController")
@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Import(CommentController.class)
    @ComponentScan(basePackageClasses = CommentMapper.class)
    @Configuration
    static class TestConfig{}

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    @DisplayName("Получить список комментариев")
    @Test
    void commentListTest() throws Exception {
        final String bookId = "1";
        final Comment comment = Comment.builder()
                .id(UUID.randomUUID().toString())
                .dateTime(LocalDateTime.now().withNano(0))
                .text("comment1")
                .build();

        when(commentService.findByBookId(eq(bookId), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(comment), PageRequest.of(0, 1), 1));

        mockMvc.perform(get("/api/comments/" + bookId)
                .content(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content[0].text").value(comment.getText()))
            .andExpect(jsonPath("$.content[0].dateTime").exists())
            .andExpect(jsonPath("$.content[0].id").value(comment.getId()));
    }

    @DisplayName("Создать комментарий")
    @Test
    void commentCreateTest() throws Exception {
        final String bookId = "1";
        final Comment comment = Comment.builder()
                .id(UUID.randomUUID().toString())
                .dateTime(LocalDateTime.now().withNano(0))
                .text("comment1")
                .build();

        mockMvc.perform(post("/api/comments/" + bookId)
                .content(objectMapper.writeValueAsString(comment))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.text").value(comment.getText()))
                .andExpect(jsonPath("$.dateTime").exists())
                .andExpect(jsonPath("$.id").value(comment.getId()));

        verify(commentService).create(bookId, comment);
    }

    @DisplayName("Обновить комментарий")
    @Test
    void commentUpdateTest() throws Exception {
        final Comment comment = Comment.builder()
                .id(UUID.randomUUID().toString())
                .dateTime(LocalDateTime.now().withNano(0))
                .text("comment1")
                .build();

        mockMvc.perform(put("/api/comment/" + comment.getId())
                .content(objectMapper.writeValueAsString(comment))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value(comment.getText()))
                .andExpect(jsonPath("$.dateTime").exists())
                .andExpect(jsonPath("$.id").value(comment.getId()));

        verify(commentService).update(comment.getId(), comment);
    }

    @DisplayName("Удалить книгу")
    @Test
    void commentDeleteTest() throws Exception {
        final String bookId = "1";

        mockMvc.perform(delete("/api/comment/" + bookId))
                .andExpect(status().isOk());

        verify(commentService).deleteById(bookId);
    }

}