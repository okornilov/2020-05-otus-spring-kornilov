package ru.otus.okornilov.homework14.chandgelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import com.mongodb.client.MongoDatabase;
import ru.otus.okornilov.homework14.domain.Author;
import ru.otus.okornilov.homework14.domain.Book;
import ru.otus.okornilov.homework14.domain.Genre;

import java.util.List;
import java.util.UUID;

@ChangeLog(order = "001")
public class InitMongoDBDataChangeLog {

    @ChangeSet(order = "001", id = "dropDB", author = "okornilov", runAlways = true)
    public void dropDB(MongoDatabase database){
        database.drop();
    }

    @ChangeSet(order = "002", id = "initBooks", author = "okornilov", runAlways = true)
    public void initBooks(MongockTemplate template){
        template.save(Book.builder()
                .name("TestBook")
                .authors(List.of(
                        Author.builder()
                                .fullName("Petrov1")
                                .id(UUID.randomUUID().toString())
                                .build(),
                        Author.builder()
                                .id(UUID.randomUUID().toString())
                                .fullName("Ivanov1")
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
                .build());

        template.save(Book.builder()
                .name("TestBook2")
                .authors(List.of(
                        Author.builder()
                                .fullName("Petrov2")
                                .id(UUID.randomUUID().toString())
                                .build(),
                        Author.builder()
                                .id(UUID.randomUUID().toString())
                                .fullName("Ivanov2")
                                .build()))
                .genres(List.of(
                        Genre.builder()
                                .id(UUID.randomUUID().toString())
                                .name("Genre3")
                                .build(),
                        Genre.builder()
                                .id(UUID.randomUUID().toString())
                                .name("Genre4")
                                .build()
                ))
                .build());
    }
}
