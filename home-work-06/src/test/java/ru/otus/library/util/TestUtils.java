package ru.otus.library.util;

import lombok.NonNull;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Comment;
import ru.otus.library.domain.Genre;

public final class TestUtils {

    private TestUtils() {
    }

    public static Book createBook(@NonNull String name, @NonNull String authorFullName, @NonNull String genreName) {
        return Book.builder()
                .name(name)
                .author(Author.builder()
                        .fullName(authorFullName)
                        .build())
                .genre(Genre.builder()
                        .name(genreName)
                        .build())
                .build();
    }

    public static Comment createComment(@NonNull String comment, @NonNull Book book) {
        return Comment.builder()
                .comment(comment)
                .book(book)
                .build();
    }
}
