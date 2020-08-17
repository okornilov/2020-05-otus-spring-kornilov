package ru.otus.library.repository;

import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;
import ru.otus.library.domain.Book;
import ru.otus.library.dto.Page;

public interface BookRepositoryCustom {
    Mono<Page<Book>> findAll(Pageable pageable);
}
