package ru.otus.library.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.library.domain.Genre;
import ru.otus.library.exceptions.EntityNotFound;
import ru.otus.library.mapper.GenreMapper;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class GenreDaoImpl implements GenreDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;
    private final GenreMapper genreMapper;

    @Override
    public Genre save(Genre genre) {
        if (genre.getId() == null) {
            Optional<Genre> optionalGenre = findByName(genre.getName());
            if (optionalGenre.isPresent()) {
                Genre foundGenre = optionalGenre.get();
                log.info("Found genre id = {} by name = {}", foundGenre.getId(), foundGenre.getName());
                return foundGenre;
            }
            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
            SqlParameterSource parameterSource = new MapSqlParameterSource()
                    .addValue("name", genre.getName());

            namedParameterJdbcOperations.update("insert into genre (name) values (:name)",
                    parameterSource, keyHolder);
            long genreId = Objects.requireNonNull(keyHolder.getKey()).longValue();
            log.info("Created a new genre with ID = {}", genreId);
            genre.setId(genreId);
            return genre;
        } else {
            Genre genreForUpdate = findById(genre.getId()).orElseThrow(() ->
                    new EntityNotFound(String.format("Genre not found by id = %s", genre.getId())));
            genreForUpdate.setName(genre.getName());
            namedParameterJdbcOperations.update("update genre set name = :name where id = :id",
                    Map.of("id", genre.getId(), "name", genre.getName()));
            log.info("Updated genre with ID = {}. Set name = {}", genreForUpdate.getId(),
                    genreForUpdate.getName());
            return genreForUpdate;
        }
    }

    @Override
    public List<Genre> findAll() {
        return namedParameterJdbcOperations.query("select * from genre", genreMapper);
    }

    @Override
    public Optional<Genre> findById(long id) {
        return namedParameterJdbcOperations
                .query("select * from genre where id = :id", Map.of("id", id), genreMapper)
                .stream().findFirst();
    }

    @Override
    public Optional<Genre> findByName(String name) {
        return namedParameterJdbcOperations
                .query("select * from genre where name = :name", Map.of("name", name), genreMapper)
                .stream().findFirst();
    }

    @Override
    public void deleteById(long id) {
        namedParameterJdbcOperations.update("delete from genre where id = :id",
                Map.of("id", id));
        log.info("Deleted genre with id = {}", id);
    }

    @Override
    public void deleteAll() {
        namedParameterJdbcOperations.getJdbcOperations().update("delete from genre");
        log.info("Deleted all genres");
    }
}
