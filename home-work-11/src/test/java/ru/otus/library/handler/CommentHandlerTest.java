package ru.otus.library.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.otus.library.domain.Comment;
import ru.otus.library.dto.CommentDto;
import ru.otus.library.dto.Page;
import ru.otus.library.mapper.CommentMapper;
import ru.otus.library.repository.CommentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Тестирование CommentHandler")
@SpringBootTest
class CommentHandlerTest {

    @Autowired
    private RouterFunction<ServerResponse> routerFunction;

    @Autowired
    private CommentMapper commentMapper;

    @MockBean
    private CommentRepository commentRepository;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient
                .bindToRouterFunction(routerFunction)
                .build();
    }

    @DisplayName("Получить список комментариев")
    @Test
    void commentListTest() {
        final String bookId = "1";
        final CommentDto commentDto = CommentDto.builder()
                .id(UUID.randomUUID().toString())
                .dateTime(LocalDateTime.now().withNano(0))
                .text("comment1")
                .build();

        Page<Comment> page = new Page<>(List.of(commentMapper.fromDto(commentDto)), 0, 1, 1);
        when(commentRepository.findByBookId(eq(bookId), any(Pageable.class)))
                .thenReturn(Mono.just(page));

        webTestClient.get()
                .uri("/api/comments/" + bookId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.pageNumber").isEqualTo(page.getPageNumber())
                .jsonPath("$.pageSize").isEqualTo(page.getPageSize())
                .jsonPath("$.totalElements").isEqualTo(page.getTotalElements())
                .jsonPath("$.content").isArray()
                .jsonPath("$.content[0].text").isEqualTo(commentDto.getText())
                .jsonPath("$.content[0].dateTime").isNotEmpty()
                .jsonPath("$.content[0].id").isEqualTo(commentDto.getId());
    }

    @DisplayName("Создать комментарий")
    @Test
    void commentCreateTest() throws Exception {
        final String bookId = "1";
        final CommentDto commentDto = CommentDto.builder()
                .id(UUID.randomUUID().toString())
                .dateTime(LocalDateTime.now().withNano(0))
                .text("comment1")
                .build();

        when(commentRepository.create(eq(bookId), any(Comment.class)))
                .thenReturn(Mono.just(commentMapper.fromDto(commentDto)));

        webTestClient.post()
                .uri("/api/comments/" + bookId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(commentDto)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.text").isEqualTo(commentDto.getText())
                .jsonPath("$.id").isEqualTo(commentDto.getId())
                .jsonPath("$.dateTime").isNotEmpty();
    }

    @DisplayName("Обновить комментарий")
    @Test
    void commentUpdateTest() {
        final CommentDto commentDto = CommentDto.builder()
                .id(UUID.randomUUID().toString())
                .dateTime(LocalDateTime.now().withNano(0))
                .text("comment1")
                .build();

        when(commentRepository.update(eq(commentDto.getId()), any(Comment.class)))
                .thenReturn(Mono.just(commentMapper.fromDto(commentDto)));

        webTestClient.put()
                .uri("/api/comments/" + commentDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(commentDto)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.text").isEqualTo(commentDto.getText())
                .jsonPath("$.id").isEqualTo(commentDto.getId())
                .jsonPath("$.dateTime").isNotEmpty();
    }

    @DisplayName("Удалить книгу")
    @Test
    void commentDeleteTest() throws Exception {
        final String commentId = "1";

        when(commentRepository.deleteById(commentId))
                .thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/comments/" + commentId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        verify(commentRepository).deleteById(commentId);
    }

}