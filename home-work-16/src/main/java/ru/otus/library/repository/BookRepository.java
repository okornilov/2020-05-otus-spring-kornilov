package ru.otus.library.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.otus.library.domain.Book;

@RepositoryRestResource(path = "books")
public interface BookRepository extends MongoRepository<Book, String> {
}
