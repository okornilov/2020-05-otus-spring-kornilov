package ru.otus.library.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.library.service.CommentService;

@RequiredArgsConstructor
@ShellComponent
public class CommentCommands {

    private final CommentService commentService;

    @ShellMethod(value = "Comment list", key = {"comment-list", "comments", "c"})
    public void list() {
        commentService.showTable();
    }

    @ShellMethod(value = "Add comment", key = {"add-comment", "ac"})
    public void add(Long bookId, String comment) {
        commentService.createComment(bookId, comment);
    }

    @ShellMethod(value = "Update comment", key = {"update-comment", "uc"})
    public void updateGenre(long id, String comment) {
        commentService.updateComment(id, comment);
    }

    @ShellMethod(value = "Delete comment", key = {"delete-comment", "dc"})
    public void delete(long id) {
        commentService.deleteById(id);
    }

    @ShellMethod(value = "Delete all comments", key = {"delete-all-comments","dac"})
    public void deleteAll() {
        commentService.deleteAll();
    }
}
