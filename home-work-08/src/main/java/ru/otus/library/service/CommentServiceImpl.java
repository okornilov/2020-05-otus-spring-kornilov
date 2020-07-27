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

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final IOService ioService;
    private final MessageBundleService bundleService;

    @Override
    public void create(String bookId, Comment comment) {
        commentRepository.add(bookId, comment);
    }

    @Override
    public void update(@NonNull String id, Comment comment) {
        commentRepository.update(id, comment);
    }

    @Override
    public void showTable(Pageable pageable) {
        showTable(commentRepository.findAll(pageable));
    }

    @Override
    public void showTable(@NonNull String bookId, Pageable pageable) {
        showTable(commentRepository.findByBookId(bookId, pageable));
    }

    @Override
    public void deleteById(@NonNull String id) {
        commentRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        commentRepository.deleteAll();
    }

    private void showTable(Page<CommentDto> page) {
        ioService.outLine(bundleService.getMessage("comment.list") + ":");
        AsciiTable asciiTable = new AsciiTable();
        asciiTable.addRule();

        asciiTable.addRow(
                bundleService.getMessage("comment.id"), bundleService.getMessage("book.id"),
                bundleService.getMessage("book.name"), bundleService.getMessage("comment.date"),
                bundleService.getMessage("comment.comment"));

        page.getContent().forEach(c -> {
            asciiTable.addRule();
            asciiTable.addRow(c.getId(), c.getBookId(), c.getBookName(), c.getDateTime(), c.getText());
        });

        asciiTable.addRule();
        ioService.outLine(asciiTable.render());
        ioService.outLine(bundleService.getMessage("page", page.getNumber() + 1, page.getTotalPages()));
        ioService.outLine(bundleService.getMessage("records", page.getNumberOfElements()));
    }
}
