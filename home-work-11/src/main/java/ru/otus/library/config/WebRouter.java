package ru.otus.library.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import ru.otus.library.handler.BookHandler;
import ru.otus.library.handler.CommentHandler;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_HTML;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@RequiredArgsConstructor
@EnableWebFlux
@Configuration
public class WebRouter {

    private final BookHandler bookHandler;
    private final CommentHandler commentHandler;

    @Value("classpath:/public/index.html")
    private Resource html;

    @Bean
    public RouterFunction<ServerResponse> routerFunction () {
        return RouterFunctions.route()
                .GET("/api/books", bookHandler::findAll)
                .POST("/api/books", accept(APPLICATION_JSON), bookHandler::create)
                .PUT("/api/books/{id}", accept(APPLICATION_JSON), bookHandler::update)
                .GET("/api/books/{id}", bookHandler::findById)
                .DELETE("/api/books/{id}", accept(APPLICATION_JSON), bookHandler::delete)
                .GET("/api/comments/{bookId}", commentHandler::findAll)
                .POST("/api/comments/{bookId}", accept(APPLICATION_JSON), commentHandler::create)
                .PUT("/api/comments/{id}", accept(APPLICATION_JSON), commentHandler::update)
                .DELETE("/api/comments/{id}", accept(APPLICATION_JSON), commentHandler::delete)
                .resources("/**", new ClassPathResource("public/"))
                .GET("/", request -> ok().contentType(TEXT_HTML).bodyValue(html))
                .GET("/comments/**", request -> ok().contentType(TEXT_HTML).bodyValue(html))
                .build();
    }

}
