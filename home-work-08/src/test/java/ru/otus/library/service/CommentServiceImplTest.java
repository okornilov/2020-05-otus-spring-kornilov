package ru.otus.library.service;

import de.vandermeer.asciitable.AsciiTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.library.domain.Comment;
import ru.otus.library.dto.CommentDto;
import ru.otus.library.repository.BookRepository;
import ru.otus.library.repository.CommentRepository;

import java.util.List;

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
        String bookId  = "1";
        Comment comment = Comment.builder().text("comment").build();
        commentService.create(bookId, comment);
        verify(commentRepository).add(bookId, comment);
    }

    @DisplayName("Обновить комментарий")
    @Test
    void update() {
        Comment comment = Comment.builder().id("1").text("comment").build();
        commentService.update(comment.getId(), comment);
        verify(commentRepository).update(comment.getId(), comment);
    }

    @DisplayName("Показать таблицу комментариев")
    @Test
    void showTable() {
        when(bundleService.getMessage("comment.list")).thenReturn("comment list");
        when(bundleService.getMessage("comment.id")).thenReturn("comment_id");
        when(bundleService.getMessage("book.id")).thenReturn("book_id");
        when(bundleService.getMessage("book.name")).thenReturn("book_name");
        when(bundleService.getMessage("comment.date")).thenReturn("comment_date");
        when(bundleService.getMessage("comment.comment")).thenReturn("comment");

        List<CommentDto> commentList = List.of(
                CommentDto.builder()
                        .id("1")
                        .text("Comment1")
                        .bookId("b1")
                        .bookName("bName1")
                        .build(),
                CommentDto.builder()
                        .id("1")
                        .text("Comment2")
                        .bookId("b2")
                        .bookName("bName2")
                        .build());

        final PageRequest pageRequest = PageRequest.of(0, commentList.size());
        final PageImpl<CommentDto> page = new PageImpl<>(commentList, pageRequest, commentList.size());

        when(commentRepository.findAll(pageRequest))
                .thenReturn(page);

        AsciiTable asciiTable = new AsciiTable();
        asciiTable.addRule();
        asciiTable.addRow(List.of("comment_id", "book_id", "book_name", "comment_date", "comment"));
        commentList.forEach(c -> {
            asciiTable.addRule();
            asciiTable.addRow(c.getId(), c.getBookId(), c.getBookName(), c.getDateTime(), c.getText());
        });
        asciiTable.addRule();

        commentService.showTable(pageRequest);
        verify(ioService).outLine("comment list:");
        verify(ioService).outLine(asciiTable.render());
    }

    @DisplayName("Удалить комментарий по идентификатору")
    @Test
    void deleteById() {
        commentService.deleteById("1");
        verify(commentRepository).deleteById("1");
    }

    @DisplayName("Удалить все комментарии")
    @Test
    void deleteAll() {
        commentService.deleteAll();
        verify(commentRepository).deleteAll();
    }
}