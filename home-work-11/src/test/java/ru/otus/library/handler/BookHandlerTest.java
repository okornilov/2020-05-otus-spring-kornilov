package ru.otus.library.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.otus.library.domain.Book;
import ru.otus.library.dto.AuthorDto;
import ru.otus.library.dto.BookDto;
import ru.otus.library.dto.GenreDto;
import ru.otus.library.dto.Page;
import ru.otus.library.mapper.BookMapper;
import ru.otus.library.repository.BookRepository;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Тестирование BookHandler")
@SpringBootTest
class BookHandlerTest {

    @Autowired
    private RouterFunction<ServerResponse> routerFunction;

    @Autowired
    private BookMapper bookMapper;

    @MockBean
    private BookRepository bookRepository;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient
                .bindToRouterFunction(routerFunction)
                .build();
    }

    @DisplayName("Создать книгу")
    @Test
    void createTest() {
        final GenreDto genreDto = GenreDto.builder()
                .id(UUID.randomUUID().toString())
                .name("Genre")
                .build();

        final AuthorDto authorDto = AuthorDto.builder()
                .id(UUID.randomUUID().toString())
                .fullName("FullName")
                .build();

        final BookDto bookDto = BookDto.builder()
                .name("book1")
                .genres(List.of(genreDto))
                .authors(List.of(authorDto))
                .build();

        Mockito.when(bookRepository.save(Mockito.any()))
                .thenReturn(Mono.just(bookMapper.fromDto(bookDto)));

        webTestClient.post()
                .uri("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bookDto)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo(bookDto.getName())
                .jsonPath("$.id").isEqualTo(bookDto.getId())
                .jsonPath("$.authors").isArray()
                .jsonPath("$.authors[0].id").isEqualTo(authorDto.getId())
                .jsonPath("$.authors[0].fullName").isEqualTo(authorDto.getFullName())
                .jsonPath("$.genres").isArray()
                .jsonPath("$.genres[0].id").isEqualTo(genreDto.getId())
                .jsonPath("$.genres[0].name").isEqualTo(genreDto.getName());
    }

    @DisplayName("Обновить книгу")
    @Test
    void updateTest() {
        final BookDto bookDto = BookDto.builder()
                .id(UUID.randomUUID().toString())
                .name("book1")
                .build();

        when(bookRepository.save(Mockito.any()))
                .thenReturn(Mono.just(bookMapper.fromDto(bookDto)));

        webTestClient.put()
                .uri("/api/books/" + bookDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bookDto)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo(bookDto.getName())
                .jsonPath("$.id").isEqualTo(bookDto.getId());
    }

    @DisplayName("Получить список книг")
    @Test
    void findAllTest() {
        final AuthorDto authorDto = AuthorDto.builder()
                .id(UUID.randomUUID().toString())
                .fullName("Author1")
                .build();

        final GenreDto genreDto = GenreDto.builder()
                .id(UUID.randomUUID().toString())
                .name("Genre")
                .build();

        final BookDto bookDto = BookDto.builder()
                .id(UUID.randomUUID().toString())
                .name("book")
                .authors(List.of(authorDto))
                .genres(List.of(genreDto))
                .build();

        final Page<Book> page = new Page<>(List.of(bookMapper.fromDto(bookDto)), 0, 1, 1);
        when(bookRepository.findAll(any(Pageable.class)))
                .thenReturn(Mono.just(page));

        webTestClient.get()
                .uri("/api/books/")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.pageNumber").isEqualTo(page.getPageNumber())
                .jsonPath("$.pageSize").isEqualTo(page.getPageSize())
                .jsonPath("$.totalElements").isEqualTo(page.getTotalElements())
                .jsonPath("$.content").isArray()
                .jsonPath("$.content[0].name").isEqualTo(bookDto.getName())
                .jsonPath("$.content[0].id").isEqualTo(bookDto.getId())
                .jsonPath("$.content[0].authors").isArray()
                .jsonPath("$.content[0].authors[0].id").isEqualTo(authorDto.getId())
                .jsonPath("$.content[0].authors[0].fullName").isEqualTo(authorDto.getFullName())
                .jsonPath("$.content[0].genres").isArray()
                .jsonPath("$.content[0].genres[0].id").isEqualTo(genreDto.getId())
                .jsonPath("$.content[0].genres[0].name").isEqualTo(genreDto.getName());

        verify(bookRepository).findAll(any(Pageable.class));
    }

    @DisplayName("Получить книгу по идентификатору")
    @Test
    void findByIdTest() {
        final String bookId = "1";
        final AuthorDto authorDto = AuthorDto.builder()
                .id(UUID.randomUUID().toString())
                .fullName("Author1")
                .build();
        final GenreDto genreDto = GenreDto.builder()
                .id(UUID.randomUUID().toString())
                .name("Genre")
                .build();
        final BookDto bookDto = BookDto.builder()
                .id(bookId)
                .name("book1")
                .authors(List.of(authorDto))
                .genres(List.of(genreDto))
                .build();

        when(bookRepository.findById(bookId))
                .thenReturn(Mono.just(bookMapper.fromDto(bookDto)));

        webTestClient.get()
                .uri("/api/books/" + bookId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo(bookDto.getName())
                .jsonPath("$.id").isEqualTo(bookDto.getId())
                .jsonPath("$.authors").isArray()
                .jsonPath("$.authors[0].id").isEqualTo(authorDto.getId())
                .jsonPath("$.authors[0].fullName").isEqualTo(authorDto.getFullName())
                .jsonPath("$.genres").isArray()
                .jsonPath("$.genres[0].id").isEqualTo(genreDto.getId())
                .jsonPath("$.genres[0].name").isEqualTo(genreDto.getName());

        verify(bookRepository).findById(bookId);
    }

    @DisplayName("Удалить книгу")
    @Test
    void deleteTest() {
        final String bookId = "1";

        when(bookRepository.deleteById(bookId))
                .thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/books/" + bookId)
                .exchange()
                .expectStatus()
                .isOk();

        verify(bookRepository).deleteById(bookId);
    }
}