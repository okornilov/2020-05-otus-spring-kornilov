package ru.otus.library.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.library.domain.Comment;
import ru.otus.library.service.CommentService;

@RequiredArgsConstructor
@ShellComponent
public class CommentCommands {

    private final CommentService commentService;

    @ShellMethod(value = "All comment list", key = {"all-comment-list", "all-comments", "all-c"})
    public void list(@ShellOption(defaultValue = "0") int page, @ShellOption(defaultValue = "10") int size) {
        commentService.showTable(PageRequest.of(page, size));
    }

    @ShellMethod(value = "Comment list", key = {"comment-list", "comments", "c"})
    public void list(String bookId, @ShellOption(defaultValue = "0") int page, @ShellOption(defaultValue = "10") int size) {
        commentService.showTable(bookId, PageRequest.of(page, size));
    }

    @ShellMethod(value = "Add comment", key = {"add-comment", "ac"})
    public void add(String bookId, String comment) {
        commentService.create(bookId, Comment.builder()
                .text(comment)
                .build());
    }

    @ShellMethod(value = "Update comment", key = {"update-comment", "uc"})
    public void updateComment(String id, String comment) {
        commentService.update(id, Comment.builder()
                .text(comment)
                .build());
    }

    @ShellMethod(value = "Delete comment", key = {"delete-comment", "dc"})
    public void delete(String id) {
        commentService.deleteById(id);
    }

    @ShellMethod(value = "Delete all comments", key = {"delete-all-comments","dac"})
    public void deleteAll() {
        commentService.deleteAll();
    }
}
