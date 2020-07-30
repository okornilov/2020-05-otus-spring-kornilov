package ru.otus.library.service;

import de.vandermeer.asciitable.AsciiTable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.otus.library.domain.Comment;
import ru.otus.library.dto.CommentDto;
import ru.otus.library.repository.CommentRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    @Override
    public void save(@NonNull String bookId, Comment comment) {
        commentRepository.save(bookId, comment);
    }

    @Override
    public Optional<Comment> findById(String id) {
        return commentRepository.findById(id);
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
