package ru.otus.library.service;

import de.vandermeer.asciitable.AsciiTable;
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

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
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

    @DisplayName("Показать таблицу книг")
    @Test
    void showTable() {
        when(bundleService.getMessage("book.list")).thenReturn("book list");
        when(bundleService.getMessage("book.id")).thenReturn("book_id");
        when(bundleService.getMessage("book.name")).thenReturn("book_name");
        when(bundleService.getMessage("authors")).thenReturn("authors");
        when(bundleService.getMessage("genres")).thenReturn("genres");

        List<Book> bookList = List.of( Book.builder()
                .id(UUID.randomUUID().toString())
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
                .build());
        PageImpl<Book> page = new PageImpl<>(bookList, PageRequest.of(0, bookList.size()), bookList.size());
        when(bookRepository.findAll(any(Pageable.class)))
                .thenReturn(page);

        AsciiTable asciiTable = new AsciiTable();
        asciiTable.addRule();
        asciiTable.addRow(List.of("book_id", "book_name","authors", "genres"));
        page.forEach(book -> {
            List<Author> authors = Optional.ofNullable(book.getAuthors()).orElse(new LinkedList<>());
            List<Genre> genres = Optional.ofNullable(book.getGenres()).orElse(new LinkedList<>());
            asciiTable.addRule();
            asciiTable.addRow(book.getId(), book.getName(),
                    authors.stream().map(Author::getFullName).collect(Collectors.joining(", ")),
                    genres.stream().map(Genre::getName).collect(Collectors.joining(", ")));
        });
        asciiTable.addRule();

        bookService.showTable(PageRequest.of(0,page.getNumberOfElements()));
        verify(ioService).outLine("book list:");
        verify(ioService).outLine(asciiTable.render());
    }

    @DisplayName("Удалить книгу по идентификатору")
    @Test
    void deleteById() {
        bookService.deleteById("id");
        verify(bookRepository).deleteById("id");
    }

    @DisplayName("Удалить все книги")
    @Test
    void deleteAll() {
        bookService.deleteAll();
        verify(bookRepository).deleteAll();
    }
}