package ru.otus.okornilov.homework13.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.okornilov.homework13.domain.Book;
import ru.otus.okornilov.homework13.repository.BookRepository;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Тестирование BookServiceImpl")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = BookServiceImpl.class)
class BookServiceImplTest {

    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private BookService bookService;

    @Test
    void createTest() {
        Book book = new Book();
        bookService.create(book);

        verify(bookRepository).save(book);
    }

    @Test
    void updateTest() {
        Book book = new Book();
        bookService.update(1L, book);

        verify(bookRepository).save(book);
    }

    @Test
    void findListTest() {
        final List<Book> books = Collections.singletonList(Book.builder().name("test").build());
        when(bookRepository.findAll()).thenReturn(books);
        assertThat(bookService.findList()).isEqualTo(books);
    }

    @Test
    void deleteTest() {
        bookService.delete(1L);
        verify(bookRepository).deleteById(1L);
    }
}