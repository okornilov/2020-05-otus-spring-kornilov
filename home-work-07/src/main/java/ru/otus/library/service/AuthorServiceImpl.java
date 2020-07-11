package ru.otus.library.service;

import de.vandermeer.asciitable.AsciiTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.domain.Author;
import ru.otus.library.repository.AuthorRepository;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

    private final IOService ioService;
    private final MessageBundleService bundleService;
    private final AuthorRepository authorRepository;

    @Transactional
    @Override
    public void save(Author author) {
        authorRepository.save(author);
    }

    @Transactional(readOnly = true)
    @Override
    public void showTable() {
        ioService.outLine(bundleService.getMessage("author.list") + ":");
        AsciiTable asciiTable = new AsciiTable();
        asciiTable.addRule();

        asciiTable.addRow(
                bundleService.getMessage("author.id"), bundleService.getMessage("author.fullName"));

        authorRepository.findAll().forEach(genre -> {
            asciiTable.addRule();
            asciiTable.addRow(genre.getId(), genre.getFullName());
        });

        asciiTable.addRule();
        ioService.outLine(asciiTable.render());
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        authorRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteAll() {
        authorRepository.deleteAll();
    }
}
