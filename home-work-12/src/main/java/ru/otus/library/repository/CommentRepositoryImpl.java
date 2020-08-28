package ru.otus.library.repository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.otus.library.domain.Comment;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class CommentRepositoryImpl implements CommentRepository {
    private final BookArrayOperations arrayOperations;
    private static final String ARRAY_NAME = "comments";

    @Override
    public void save(@NonNull String bookId, Comment comment) {
        arrayOperations.saveElement(bookId, comment, ARRAY_NAME);
    }

    @Override
    public Optional<Comment> findById(@NonNull String id) {
        return Optional.ofNullable(arrayOperations.findById(ARRAY_NAME, id, Comment.class));
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
