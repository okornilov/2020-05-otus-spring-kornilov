package ru.otus.library.service;

import de.vandermeer.asciitable.AsciiTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.library.dao.GenreDao;
import ru.otus.library.domain.Genre;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {

    private final IOService ioService;
    private final MessageBundleService bundleService;
    private final GenreDao genreDao;

    @Override
    public void save(Genre genre) {
        genreDao.save(genre);
    }

    @Override
    public void showTable() {
        ioService.outLine(bundleService.getMessage("genre.list") + ":");
        AsciiTable asciiTable = new AsciiTable();
        asciiTable.addRule();

        asciiTable.addRow(
                bundleService.getMessage("genre.id"), bundleService.getMessage("genre.name"));

        genreDao.findAll().forEach(genre -> {
            asciiTable.addRule();
            asciiTable.addRow(genre.getId(), genre.getName());
        });

        asciiTable.addRule();
        ioService.outLine(asciiTable.render());
    }

    @Override
    public void deleteById(long id) {
        genreDao.deleteById(id);
    }

    @Override
    public void deleteAll() {
        genreDao.deleteAll();
    }
}
