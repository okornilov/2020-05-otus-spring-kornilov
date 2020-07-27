package ru.otus.library.service;

import de.vandermeer.asciitable.AsciiTable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.otus.library.domain.Author;
import ru.otus.library.dto.AuthorDto;
import ru.otus.library.repository.AuthorRepository;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final IOService ioService;
    private final MessageBundleService bundleService;

    @Override
    public void create(@NonNull String bookId, Author author) {
        authorRepository.add(bookId, author);
    }

    @Override
    public void update(@NonNull String id, Author author) {
        authorRepository.update(id, author);
    }

    @Override
    public void showTable(Pageable pageable) {
        showTable(authorRepository.findAll(pageable));
    }

    @Override
    public void showTable(@NonNull String bookId, Pageable pageable) {
        showTable(authorRepository.findByBookId(bookId, pageable));
    }

    private void showTable(Page<AuthorDto> page) {
        ioService.outLine(bundleService.getMessage("author.list") + ":");
        AsciiTable asciiTable = new AsciiTable();
        asciiTable.addRule();

        asciiTable.addRow(bundleService.getMessage("author.id"), bundleService.getMessage("book.id"),
                bundleService.getMessage("book.name"), bundleService.getMessage("author.fullName"));
        page.getContent().forEach(authorDto -> {
            asciiTable.addRule();
            asciiTable.addRow(authorDto.getId(), authorDto.getBookId(), authorDto.getBookName(), authorDto.getFullName());
        });

        asciiTable.addRule();
        ioService.outLine(asciiTable.render());
        ioService.outLine(bundleService.getMessage("page", page.getNumber() + 1, page.getTotalPages()));
        ioService.outLine(bundleService.getMessage("records", page.getTotalElements()));
    }

    @Override
    public void deleteById(@NonNull String id) {
        authorRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        authorRepository.deleteAll();
    }
}
