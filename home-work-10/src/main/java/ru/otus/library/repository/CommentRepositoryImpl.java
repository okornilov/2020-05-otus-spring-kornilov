package ru.otus.library.repository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.otus.library.domain.Comment;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class CommentRepositoryImpl implements CommentRepository {
    private final BookArrayOperations arrayOperations;
    private static final String ARRAY_NAME = "comments";

    @Override
    public void create(@NonNull String bookId, Comment comment) {
        if (comment.getDateTime() == null) {
            comment.setDateTime(LocalDateTime.now().withNano(0));
        }
        arrayOperations.addElement(bookId, comment, ARRAY_NAME);
    }

    @Override
    public void update(@NonNull String id, Comment comment) {
        comment.setId(id);
        arrayOperations.updateElement(id, comment, ARRAY_NAME);
    }

    @Override
    public Page<Comment> findByBookId(@NonNull String bookId, Pageable pageable) {
        return arrayOperations.findElements(ARRAY_NAME, bookId, pageable, Comment.class);
    }

    @Override
    public void deleteById(@NonNull String id) {
        arrayOperations.deleteElement(id, ARRAY_NAME);
    }

}
