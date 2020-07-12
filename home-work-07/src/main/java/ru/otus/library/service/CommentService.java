package ru.otus.library.service;

import lombok.NonNull;

public interface CommentService {
    void createComment(long bookId, @NonNull String comment);
    void updateComment(long commentId, @NonNull String comment);
    void showTable();
    void deleteById(long id);
    void deleteAll();
}
