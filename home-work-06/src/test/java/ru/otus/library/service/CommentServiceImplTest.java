package ru.otus.library.service;

import de.vandermeer.asciitable.AsciiTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Comment;
import ru.otus.library.repository.BookRepository;
import ru.otus.library.repository.CommentRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Тестирование CommentServiceImplTest")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CommentServiceImpl.class)
class CommentServiceImplTest {

    @Autowired
    private CommentService commentService;

    @MockBean
    private IOService ioService;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private MessageBundleService bundleService;

    @DisplayName("Создать комментарий")
    @Test
    void create() {
        Book book = Book.builder()
                .id(1L)
                .build();
        when(bookRepository.findById(book.getId()))
                .thenReturn(Optional.of(book));

        String comment = "testComment";

        commentService.createComment(book.getId(), comment);
        verify(commentRepository).save(argThat(c ->
            c.getBook().equals(book) && c.getComment().equals(comment)
        ));
    }

    @DisplayName("Обновить комментарий")
    @Test
    void update() {
        Book book = Book.builder()
                .id(1L)
                .build();
        Comment comment = Comment.builder()
                .id(2L)
                .book(book)
                .comment("testComment")
                .build();
        when(commentRepository.findById(comment.getId()))
                .thenReturn(Optional.of(comment));

        String newComment = "newComment";
        commentService.updateComment(comment.getId(), newComment);
        verify(commentRepository).save(argThat(c -> c.getComment().equals(newComment)));
    }

    @DisplayName("Показать таблицу комментариев")
    @Test
    void showTable() {
        when(bundleService.getMessage("comment.list")).thenReturn("comment list");
        when(bundleService.getMessage("comment.id")).thenReturn("comment_id");
        when(bundleService.getMessage("book.id")).thenReturn("book_id");
        when(bundleService.getMessage("comment.comment")).thenReturn("comment");
        List<Comment> commentList = List.of(
                Comment.builder()
                        .id(1L)
                        .comment("Comment1")
                        .book(Book.builder().id(1L).build()).build(),
                Comment.builder()
                        .id(2L)
                        .comment("Comment2")
                        .book(Book.builder().id(2L).build())
                        .build());
        when(commentRepository.findAll()).thenReturn(commentList);

        AsciiTable asciiTable = new AsciiTable();
        asciiTable.addRule();
        asciiTable.addRow(List.of("comment_id", "book_id", "comment"));
        commentList.forEach(comment -> {
            asciiTable.addRule();
            asciiTable.addRow(comment.getId(), comment.getBook().getId(), comment.getComment());
        });
        asciiTable.addRule();

        commentService.showTable();
        verify(ioService).outLine("comment list:");
        verify(ioService).outLine(asciiTable.render());
    }

    @DisplayName("Удалить комментарий по идентификатору")
    @Test
    void deleteById() {
        commentService.deleteById(1L);
        verify(commentRepository).deleteById(1L);
    }

    @DisplayName("Удалить все комментарии")
    @Test
    void deleteAll() {
        commentService.deleteAll();
        verify(commentRepository).deleteAll();
    }
}