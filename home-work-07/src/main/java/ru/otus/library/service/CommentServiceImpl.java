package ru.otus.library.service;

import de.vandermeer.asciitable.AsciiTable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Comment;
import ru.otus.library.exceptions.EntityNotFound;
import ru.otus.library.repository.BookRepository;
import ru.otus.library.repository.CommentRepository;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final IOService ioService;
    private final MessageBundleService bundleService;
    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;

    @Transactional
    @Override
    public void createComment(long bookId, @NonNull String comment) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFound("Book not found"));
        commentRepository.save(Comment.builder()
                .comment(comment)
                .book(book)
                .build());
    }

    @Transactional
    @Override
    public void updateComment(long commentId, @NonNull String comment) {
        Comment commentEntity = commentRepository.findById(commentId).orElseThrow(() -> new EntityNotFound("Comment not found"));
        commentEntity.setComment(comment);
        commentRepository.save(commentEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public void showTable() {
        ioService.outLine(bundleService.getMessage("comment.list") + ":");
        AsciiTable asciiTable = new AsciiTable();
        asciiTable.addRule();

        asciiTable.addRow(
                bundleService.getMessage("comment.id"), bundleService.getMessage("book.id"), bundleService.getMessage("comment.comment"));

        commentRepository.findAll().forEach(genre -> {
            asciiTable.addRule();
            asciiTable.addRow(genre.getId(), genre.getBook().getId(), genre.getComment());
        });

        asciiTable.addRule();
        ioService.outLine(asciiTable.render());
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteAll() {
        commentRepository.deleteAll();
    }
}
