package ru.otus.library.service;

import de.vandermeer.asciitable.AsciiTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;
import ru.otus.library.repository.BookRepository;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Тестирование BookServiceImpl")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = BookServiceImpl.class)
class BookServiceImplTest {

    @Autowired
    private BookService bookService;

    @MockBean
    private IOService ioService;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private MessageBundleService bundleService;

    @DisplayName("Сохранить книгу")
    @Test
    void save() {
        Book book = Book.builder()
                .id(1L)
                .name("Book")
                .author(Author.builder().id(2L).fullName("FullName").build())
                .genre(Genre.builder().id(3L).name("Genre").build())
                .build();

        bookService.save(book);
        verify(bookRepository).save(book);
    }

    @DisplayName("Показать таблицу книг")
    @Test
    void showTable() {
        when(bundleService.getMessage("book.list")).thenReturn("book list");
        when(bundleService.getMessage("book.id")).thenReturn("book_id");
        when(bundleService.getMessage("book.name")).thenReturn("book_name");
        when(bundleService.getMessage("author.id")).thenReturn("author_id");
        when(bundleService.getMessage("author.fullName")).thenReturn("author_full_name");
        when(bundleService.getMessage("genre.id")).thenReturn("genre_id");
        when(bundleService.getMessage("genre.name")).thenReturn("genre_name");

        List<Book> bookList = List.of(
                Book.builder()
                        .id(1L)
                        .name("book1")
                        .author(Author.builder().id(2L).fullName("fullName1").build())
                        .genre(Genre.builder().id(4L).name("genre1").build())
                        .build(),
                Book.builder()
                        .id(2L)
                        .name("book2")
                        .author(Author.builder().id(3L).fullName("fullName2").build())
                        .genre(Genre.builder().id(5L).name("genre2").build())
                        .build());
        when(bookRepository.findAll()).thenReturn(bookList);

        AsciiTable asciiTable = new AsciiTable();
        asciiTable.addRule();
        asciiTable.addRow(List.of("book_id", "book_name","author_id", "author_full_name", "genre_id", "genre_name"));
        bookList.forEach(book -> {
            Author author = book.getAuthor();
            Genre genre = book.getGenre();
            asciiTable.addRule();
            asciiTable.addRow(book.getId(), book.getName(), author.getId(), author.getFullName(),
                    genre.getId(), genre.getName());
        });
        asciiTable.addRule();

        bookService.showTable();
        verify(ioService).outLine("book list:");
        verify(ioService).outLine(asciiTable.render());
    }

    @DisplayName("Удалить книгу по идентификатору")
    @Test
    void deleteById() {
        bookService.deleteById(1L);
        verify(bookRepository).deleteById(1L);
    }

    @DisplayName("Удалить все книги")
    @Test
    void deleteAll() {
        bookService.deleteAll();
        verify(bookRepository).deleteAll();
    }
}