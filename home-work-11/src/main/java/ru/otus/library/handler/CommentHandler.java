package ru.otus.library.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.otus.library.dto.CommentDto;
import ru.otus.library.dto.Page;
import ru.otus.library.mapper.CommentMapper;
import ru.otus.library.repository.CommentRepository;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static ru.otus.library.ServiceConstants.*;

@RequiredArgsConstructor
@Component
public class CommentHandler {
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;

    public Mono<ServerResponse> findAll(ServerRequest request) {
        final int page = request.queryParam(PAGE).map(Integer::valueOf).orElse(Page.FIRST_PAGE_NUM);
        final int size = request.queryParam(SIZE).map(Integer::valueOf).orElse(Page.DEFAULT_PAGE_SIZE);
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(commentRepository
                        .findByBookId(request.pathVariable(BOOK_ID), PageRequest.of(page, size))
                        .map(p -> p.map(commentMapper::toDto)), Page.class);
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(CommentDto.class)
                .map(commentMapper::fromDto)
                .map(comment -> commentRepository
                        .create(request.pathVariable(BOOK_ID), comment)
                        .map(commentMapper::toDto)
                )
                .flatMap(comment -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .body(comment, CommentDto.class));
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        return request.bodyToMono(CommentDto.class)
                .map(commentMapper::fromDto)
                .map(dto -> commentRepository
                        .update(request.pathVariable(ID), dto)
                        .map(commentMapper::toDto)
                )
                .flatMap(comment -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .body(comment, CommentDto.class));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        return commentRepository.deleteById(request.pathVariable(ID))
                .then(ServerResponse.ok().build());
    }
}
