package ru.otus.library.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Mono;
import ru.otus.library.domain.Book;
import ru.otus.library.dto.Page;

@RequiredArgsConstructor
public class BookRepositoryCustomImpl implements BookRepositoryCustom {
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @Override
    public Mono<Page<Book>> findAll(Pageable pageable) {
        return reactiveMongoTemplate.findAll(Book.class)
                .skip((long) pageable.getPageNumber() * pageable.getPageSize())
                .take(pageable.getPageSize())
                .collectList()
                .flatMap(books -> reactiveMongoTemplate.count(new Query(), Book.class)
                        .map(count -> new Page<>(books, pageable.getPageNumber(), pageable.getPageSize(), count)));
    }
}
