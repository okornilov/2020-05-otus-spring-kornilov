package ru.otus.library.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.otus.library.domain.Genre;
import ru.otus.library.repository.GenreRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Override
    public void save(@NonNull String bookId, Genre genre) {
        genreRepository.save(bookId, genre);
    }

    @Override
    public Optional<Genre> findById(@NonNull String id) {
        return genreRepository.findById(id);
    }

    @Override
    public Page<Genre> findByBookId(String bookId, Pageable pageable) {
        return genreRepository.findByBookId(bookId, pageable);
    }

    @Override
    public void deleteById(@NonNull String id) {
        genreRepository.deleteById(id);
    }
}
