package ru.otus.library.service;

import de.vandermeer.asciitable.AsciiTable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.otus.library.domain.Genre;
import ru.otus.library.dto.GenreDto;
import ru.otus.library.repository.GenreRepository;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;
    private final IOService ioService;
    private final MessageBundleService bundleService;

    @Override
    public void create(@NonNull String bookId, Genre genre) {
        genreRepository.add(bookId, genre);
    }

    @Override
    public void update(@NonNull String id, Genre genre) {
        genreRepository.update(id, genre);
    }

    @Override
    public void showTable(Pageable pageable) {
        showTable(genreRepository.findAll(pageable));
    }

    @Override
    public void showTable(@NonNull String bookId, Pageable pageable) {
        showTable(genreRepository.findByBookId(bookId, pageable));
    }

    @Override
    public void deleteById(@NonNull String id) {
        genreRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        genreRepository.deleteAll();
    }

    private void showTable(Page<GenreDto> page) {
        ioService.outLine(bundleService.getMessage("genre.list") + ":");
        AsciiTable asciiTable = new AsciiTable();
        asciiTable.addRule();

        asciiTable.addRow(
                bundleService.getMessage("genre.id"),
                bundleService.getMessage("book.id"),
                bundleService.getMessage("book.name"),
                bundleService.getMessage("genre.name"));

        page.getContent().forEach(g -> {
            asciiTable.addRule();
            asciiTable.addRow(g.getId(), g.getBookId(), g.getBookName(), g.getName());
        });

        asciiTable.addRule();
        ioService.outLine(asciiTable.render());
        ioService.outLine(bundleService.getMessage("page", page.getNumber() + 1, page.getTotalPages()));
        ioService.outLine(bundleService.getMessage("records", page.getTotalElements()));
    }
}
