package ru.otus.library.service;

import de.vandermeer.asciitable.AsciiTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.library.dao.AuthorDao;
import ru.otus.library.domain.Author;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

    private final IOService ioService;
    private final MessageBundleService bundleService;
    private final AuthorDao authorDao;

    @Override
    public void save(Author author) {
        authorDao.save(author);
    }

    @Override
    public void showTable() {
        ioService.outLine(bundleService.getMessage("author.list") + ":");
        AsciiTable asciiTable = new AsciiTable();
        asciiTable.addRule();

        asciiTable.addRow(
                bundleService.getMessage("author.id"), bundleService.getMessage("author.fio"));

        authorDao.findAll().forEach(genre -> {
            asciiTable.addRule();
            asciiTable.addRow(genre.getId(), genre.getFio());
        });

        asciiTable.addRule();
        ioService.outLine(asciiTable.render());
    }

    @Override
    public void deleteById(long id) {
        authorDao.deleteById(id);
    }

    @Override
    public void deleteAll() {
        authorDao.deleteAll();
    }
}
