package ru.otus.library.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.library.domain.Author;

@Repository
public interface AuthorRepository extends CrudRepository<Author, Long> {
}
