package ru.otus.library.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.library.domain.Genre;

@Repository
public interface GenreRepository extends CrudRepository<Genre, Long> {
}
