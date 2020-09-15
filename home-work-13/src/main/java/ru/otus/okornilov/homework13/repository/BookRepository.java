package ru.otus.okornilov.homework13.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Repository;
import ru.otus.okornilov.homework13.domain.Book;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    @PostFilter("hasPermission(filterObject, 'READ')")
    @EntityGraph(attributePaths = {"author"})
    @Override
    Iterable<Book> findAll();
}
