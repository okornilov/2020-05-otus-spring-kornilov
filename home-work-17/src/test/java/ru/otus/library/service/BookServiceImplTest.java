package ru.otus.library.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Comment;
import ru.otus.library.domain.Genre;
import ru.otus.library.repository.BookRepository;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Тестирование BookServiceImpl")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BookServiceImpl.class})
class BookServiceImplTest {

    @Autowired
    private BookService bookService;

    @MockBean
    private BookRepository bookRepository;

    @DisplayName("Сохранить книгу")
    @Test
    void save() {
        Book book = Book.builder()
                .name("test book")
                .authors(List.of(Author.builder()
                        .id(UUID.randomUUID().toString())
                        .fullName("author 1").build()))
                .genres(List.of(Genre.builder()
                        .id(UUID.randomUUID().toString())
                        .name("genre 1").build()))
                .comments(List.of(Comment.builder()
                        .id(UUID.randomUUID().toString())
                        .text("comment1").build()))
                .build();

        bookService.save(book);
        verify(bookRepository).save(book);
    }

    @DisplayName("Получить книгу по идентификатору")
    @Test
    void findByIdTest() {
        String bookId = "1";
        bookService.findById(bookId);
        verify(bookRepository).findById(bookId);
    }

    @DisplayName("Получить все книги")
    @Test
    void findAllTest() {
        Pageable pageable = PageRequest.of(0, 1);
        Book book = Book.builder()
                .name("book")
                .authors(List.of(Author.builder()
                        .id(UUID.randomUUID().toString())
                        .fullName("author")
                        .build()))
                .genres(List.of(Genre.builder()
                        .id(UUID.randomUUID().toString())
                        .name("genre")
                        .build()))
                .build();

        when(bookRepository.findAll(pageable))
                .thenReturn(new PageImpl<>(List.of(book), pageable, 1));

        bookService.findAll(pageable);
        verify(bookRepository).findAll(pageable);
    }

    @DisplayName("Удалить книгу по идентификатору")
    @Test
    void deleteById() {
        bookService.deleteById("id");
        verify(bookRepository).deleteById("id");
    }
}