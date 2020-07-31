package ru.otus.library.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.otus.library.domain.Author;
import ru.otus.library.repository.AuthorRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Override
    public void save(@NonNull String bookId, Author author) {
        authorRepository.save(bookId, author);
    }

    @Override
    public Optional<Author> findById(String id) {
        return authorRepository.findById(id);
    }

    @Override
    public Page<Author> findByBookId(String bookId, Pageable pageable) {
        return authorRepository.findAllByBookId(bookId, pageable);
    }

    @Override
    public void deleteById(@NonNull String id) {
        authorRepository.deleteById(id);
    }
}
