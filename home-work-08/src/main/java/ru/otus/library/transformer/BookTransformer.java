package ru.otus.library.transformer;

import org.springframework.stereotype.Component;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class BookTransformer {

    public Book transform(String name, String[] authors, String[] genres) {
        return transform(null, name, authors, genres);
    }

    public Book transform(String id, String name, String[] authors, String[] genres) {
        final List<Author> authorList = Stream.of(authors)
                .map(s -> Author.builder()
                        .id(UUID.randomUUID().toString())
                        .fullName(s)
                        .build())
                .collect(Collectors.toList());
        final List<Genre> genreList = Stream.of(genres)
                .map(g -> Genre.builder()
                        .id(UUID.randomUUID().toString())
                        .name(g)
                        .build())
                .collect(Collectors.toList());

        return Book.builder()
                .id(id)
                .name(name)
                .authors(authorList)
                .genres(genreList)
                .build();
    }

}
