package ru.otus.library.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Comment;
import ru.otus.library.domain.Genre;
import ru.otus.library.repository.BookRepository;

import java.util.List;
import java.util.UUID;

@ChangeLog(order = "001")
public class InitMongoDBDataChangeLog {

    @ChangeSet(order = "001", id = "dropDB", author = "okornilov", runAlways = true)
    public void dropDB(MongoDatabase database){
        database.drop();
    }

    @ChangeSet(order = "002", id = "initBooks", author = "okornilov", runAlways = true)
    public void initBooks(BookRepository repository){
        repository.save(Book.builder()
                .name("TestBook")
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
                                .text("Comment1")
                                .build(),
                        Comment.builder()
                                .id(UUID.randomUUID().toString())
                                .text("Comment2")
                                .build()))
                .build());
        System.out.println(repository.findAll());
    }
}
