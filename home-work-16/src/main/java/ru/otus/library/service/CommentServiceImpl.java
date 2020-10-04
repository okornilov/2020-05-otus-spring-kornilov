package ru.otus.library.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.otus.library.domain.Comment;
import ru.otus.library.repository.CommentRepository;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    @Override
    public void create(@NonNull String bookId, Comment comment) {
        commentRepository.create(bookId, comment);
    }

    @Override
    public void update(@NonNull String id, Comment comment) {
        commentRepository.update(id, comment);
    }

    @Override
    public Page<Comment> findByBookId(String bookId, Pageable pageable) {
        return commentRepository.findByBookId(bookId, pageable);
    }

    @Override
    public void deleteById(@NonNull String id) {
        commentRepository.deleteById(id);
    }
}
