package ru.otus.library.service;

import de.vandermeer.asciitable.AsciiTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.domain.Genre;
import ru.otus.library.repository.GenreRepository;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {

    private final IOService ioService;
    private final MessageBundleService bundleService;
    private final GenreRepository genreRepository;

    @Transactional
    @Override
    public void save(Genre genre) {
        genreRepository.save(genre);
    }

    @Transactional(readOnly = true)
    @Override
    public void showTable() {
        ioService.outLine(bundleService.getMessage("genre.list") + ":");
        AsciiTable asciiTable = new AsciiTable();
        asciiTable.addRule();

        asciiTable.addRow(
                bundleService.getMessage("genre.id"), bundleService.getMessage("genre.name"));

        genreRepository.findAll().forEach(genre -> {
            asciiTable.addRule();
            asciiTable.addRow(genre.getId(), genre.getName());
        });

        asciiTable.addRule();
        ioService.outLine(asciiTable.render());
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        genreRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteAll() {
        genreRepository.deleteAll();
    }
}
