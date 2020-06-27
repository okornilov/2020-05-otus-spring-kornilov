package ru.otus.library.dao;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.library.domain.Author;
import ru.otus.library.exceptions.EntityNotFound;
import ru.otus.library.mapper.AuthorMapper;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AuthorDaoImpl implements AuthorDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;
    private final AuthorMapper authorMapper;

    @Override
    public Author save(@NonNull Author author) {
        if (author.getId() == null) {
            Optional<Author> optionalAuthor = findByFio(author.getFio());
            if (optionalAuthor.isPresent()) {
                Author foundAuthor = optionalAuthor.get();
                log.info("Found author id = {} by name = {}", foundAuthor.getId(), foundAuthor.getFio());
                return foundAuthor;
            }
            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
            SqlParameterSource parameterSource = new MapSqlParameterSource()
                    .addValue("fio", author.getFio());
            namedParameterJdbcOperations.update("insert into author (fio) values (:fio)",
                    parameterSource, keyHolder);
            long authorId = Objects.requireNonNull(keyHolder.getKey()).longValue();
            log.info("Created a new author with ID = {}", authorId);
            author.setId(authorId);
            return author;
        } else {
            Author authorForUpdate = findById(author.getId()).orElseThrow(() ->
                    new EntityNotFound(String.format("Author not found by id = %s", author.getId())));
            authorForUpdate.setFio(author.getFio());
            namedParameterJdbcOperations.update("update author set fio = :fio where id = :id",
                    Map.of("id", authorForUpdate.getId(), "fio", authorForUpdate.getFio()));
            log.info("Updated author with ID = {}. Set fio = {}", authorForUpdate.getId(),
                    authorForUpdate.getFio());
            return authorForUpdate;
        }
    }

    @Override
    public List<Author> findAll() {
        return namedParameterJdbcOperations.query("select * from author", authorMapper);
    }

    @Override
    public Optional<Author> findById(long id) {
        return namedParameterJdbcOperations.query(
                "select * from author where id = :id", Map.of("id", id), authorMapper)
                .stream().findFirst();
    }

    @Override
    public Optional<Author> findByFio(String fio) {
        return namedParameterJdbcOperations.query(
                "select * from author where fio = :fio", Map.of("fio", fio), authorMapper)
                .stream().findFirst();
    }

    @Override
    public void deleteById(long id) {
        namedParameterJdbcOperations.update("delete from author where id = :id",
                Map.of("id", id));
        log.info("Deleted author with id = {}", id);
    }

    @Override
    public void deleteAll() {
        namedParameterJdbcOperations.getJdbcOperations().update("delete from author");
        log.info("Deleted all authors");
    }
}
