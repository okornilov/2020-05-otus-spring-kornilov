package ru.otus.library.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.library.domain.Comment;
import ru.otus.library.repository.CommentRepository;

import static org.mockito.Mockito.verify;

@DisplayName("Тестирование CommentServiceImplTest")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CommentServiceImpl.class)
class CommentServiceImplTest {

    @Autowired
    private CommentService commentService;

    @MockBean
    private CommentRepository commentRepository;

    @DisplayName("Сохранить комментарий")
    @Test
    void saveComment() {
        String bookId  = "1";
        Comment comment = Comment.builder().text("comment").build();
        commentService.save(bookId, comment);
        verify(commentRepository).save(bookId, comment);
    }

    @DisplayName("Получить комментарий по идентификатору")
    @Test
    void findByIdTest() {
        String commentId = "1";
        commentService.findById(commentId);
        verify(commentRepository).findById(commentId);
    }

    @DisplayName("Получить комментарии по книге")
    @Test
    void findByBookIdTest() {
        String bookId = "1";
        Pageable pageable = PageRequest.of(0, 1);
        commentService.findByBookId(bookId, pageable);
        verify(commentRepository).findByBookId(bookId, pageable);
    }

    @DisplayName("Удалить комментарий по идентификатору")
    @Test
    void deleteById() {
        commentService.deleteById("1");
        verify(commentRepository).deleteById("1");
    }

}