package ru.otus.library.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Comment;
import ru.otus.library.domain.Genre;
import ru.otus.library.repository.BookRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@ChangeLog(order = "001")
public class InitMongoDBDataChangeLog {

    @ChangeSet(order = "001", id = "libraryDropDB", author = "okornilov", runAlways = false)
    public void dropDB(MongoDatabase database){
        database.drop();
    }

    @ChangeSet(order = "002", id = "libraryInitBooks", author = "okornilov", runAlways = false)
    public void initBooks(BookRepository repository){
        IntStream.range(1, 5).forEach(value -> {
            repository.save(getBook(String.format("Book %d", value)));
        });
    }

    private Book getBook(String name) {
        return Book.builder()
                .name(name)
                .authors(List.of(
                        Author.builder()
                                .fullName("Petrov")
                                .id(UUID.randomUUID().toString())
                                .build(),
                        Author.builder()
                                .id(UUID.randomUUID().toString())
                                .fullName("Ivanov")
                                .build()))
                .genres(List.of(
                        Genre.builder()
                                .id(UUID.randomUUID().toString())
                                .name("Genre1")
                                .build(),
                        Genre.builder()
                                .id(UUID.randomUUID().toString())
                                .name("Genre2")
                                .build()
                ))
                .comments(List.of(
                        Comment.builder()
                                .id(UUID.randomUUID().toString())
                                .dateTime(LocalDateTime.now().withNano(0))
                                .text("Comment1")
                                .build(),
                        Comment.builder()
                                .id(UUID.randomUUID().toString())
                                .dateTime(LocalDateTime.now().withNano(0))
                                .text("Comment2")
                                .build()))
                .build();
    }
}
