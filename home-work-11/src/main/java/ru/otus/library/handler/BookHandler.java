package ru.otus.library.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.otus.library.ServiceConstants;
import ru.otus.library.dto.BookDto;
import ru.otus.library.dto.Page;
import ru.otus.library.mapper.BookMapper;
import ru.otus.library.repository.BookRepository;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static ru.otus.library.ServiceConstants.PAGE;
import static ru.otus.library.ServiceConstants.SIZE;

@RequiredArgsConstructor
@Component
public class BookHandler {
    private final BookMapper bookMapper;
    private final BookRepository bookRepository;

    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(BookDto.class)
                .map(bookMapper::fromDto)
                .flatMap(bookRepository::save)
                .map(bookMapper::toDto)
                .flatMap(bookMono -> ok()
                        .contentType(APPLICATION_JSON)
                        .body(Mono.just(bookMono), BookDto.class));
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        return request.bodyToMono(BookDto.class)
                .map(bookMapper::fromDto)
                .flatMap(book -> {
                    book.setId(request.pathVariable(ServiceConstants.ID));
                    return bookRepository.save(book);
                })
                .map(bookMapper::toDto)
                .flatMap(bookMono -> ok()
                        .contentType(APPLICATION_JSON)
                        .body(Mono.just(bookMono), BookDto.class));
    }

    public Mono<ServerResponse> findAll(ServerRequest request) {
        final int page = request.queryParam(PAGE).map(Integer::valueOf).orElse(Page.FIRST_PAGE_NUM);
        final int size = request.queryParam(SIZE).map(Integer::valueOf).orElse(Page.DEFAULT_PAGE_SIZE);
        return ok().contentType(APPLICATION_JSON)
                .body(bookRepository
                        .findAll(PageRequest.of(page, size))
                        .map(p -> p.map(bookMapper::toDto)), Page.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        return bookRepository
                .findById(request.pathVariable(ServiceConstants.ID))
                .map(bookMapper::toDto)
                .flatMap(book -> ok().contentType(APPLICATION_JSON).body(Mono.just(book), BookDto.class))
                .switchIfEmpty(notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        return bookRepository.deleteById(request.pathVariable(ServiceConstants.ID))
                .then(ok().contentType(APPLICATION_JSON).build());
    }
}
