package ru.otus.library.repository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.otus.library.domain.Comment;
import ru.otus.library.dto.CommentDto;

@RequiredArgsConstructor
@Repository
public class CommentRepositoryImpl implements CommentRepository {
    private final BookRepository bookRepository;
    private static final String ARRAY_NAME = "comments";

    @Override
    public void add(@NonNull String bookId, Comment comment) {
        bookRepository.addArrayElement(bookId, comment, ARRAY_NAME);
    }

    @Override
    public void update(@NonNull String id, Comment comment) {
        bookRepository.updateArrayElement(id, comment, ARRAY_NAME);
    }

    @Override
    public Page<CommentDto> findAll(Pageable pageable) {
        return bookRepository.findElementsByArrayName(ARRAY_NAME,null, pageable, CommentDto.class);
    }

    @Override
    public Page<CommentDto> findByBookId(@NonNull String bookId, Pageable pageable) {
        return bookRepository.findElementsByArrayName(ARRAY_NAME, bookId, pageable, CommentDto.class);
    }

    @Override
    public void deleteById(@NonNull String id) {
        bookRepository.deleteElementInArray(id, ARRAY_NAME);
    }

    @Override
    public void deleteAll() {
        bookRepository.deleteAllElementsInArray(ARRAY_NAME);
    }
}
