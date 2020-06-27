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
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;
import ru.otus.library.exceptions.EntityNotFound;
import ru.otus.library.mapper.BookMapper;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class BookDaoImpl implements BookDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;
    private final BookMapper bookMapper;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;

    @Override
    public Book save(@NonNull Book book) {
        Author author = Optional.ofNullable(book.getAuthor()).orElse(new Author());
        Genre genre = Optional.ofNullable(book.getGenre()).orElse(new Genre());

        if (book.getId() == null) {
            Optional<Book> optionalBook = findByName(book.getName());
            if (optionalBook.isPresent()) {
                Book foundBook = optionalBook.get();
                log.info("Found book id = {} by name = {}", foundBook.getId(), foundBook.getName());
                return foundBook;
            }

            Author authorForCreate = authorDao.save(author);
            Genre genreForCreate = genreDao.save(genre);

            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
            SqlParameterSource parameterSource = new MapSqlParameterSource()
                    .addValue("name", book.getName())
                    .addValue("author_id", authorForCreate.getId())
                    .addValue("genre_id", genreForCreate.getId());
            namedParameterJdbcOperations.update("insert into book (name, author_id, genre_id) " +
                            "values (:name, :author_id, :genre_id)",
                    parameterSource, keyHolder);
            long bookId = Objects.requireNonNull(keyHolder.getKey()).longValue();
            log.info("Created a new book with ID = {}", bookId);
            book.setId(bookId);
            return book;
        } else {
            Book bookForUpdate = findById(book.getId()).orElseThrow(() ->
                    new EntityNotFound(String.format("Book not found by id = %s", book.getId())));
            bookForUpdate.setName(book.getName());

            Author authorForUpdate = authorDao.save(author);
            Genre genreForUpdate = genreDao.save(genre);

            bookForUpdate.setAuthor(authorForUpdate);
            bookForUpdate.setGenre(genreForUpdate);

            namedParameterJdbcOperations.update("update book " +
                            "set name = :name, " +
                            "author_id = :author_id, " +
                            "genre_id = :genre_id " +
                            "where id = :id",
                    Map.of("id", bookForUpdate.getId(),
                            "name", bookForUpdate.getName(),
                            "author_id", authorForUpdate.getId(),
                            "genre_id", genreForUpdate.getId()));
            log.info("Updated book with ID = {}. Set name = {}", bookForUpdate.getId(),
                    bookForUpdate.getName());
            return bookForUpdate;
        }
    }

    @Override
    public void deleteById(long id) {
        namedParameterJdbcOperations.update("delete from book where id = :id", Map.of("id", id));
        log.info("Deleted book with id = {}", id);
    }

    @Override
    public void deleteAll() {
        namedParameterJdbcOperations.getJdbcOperations().update("delete from book");
        log.info("Deleted all books");

    }

    @Override
    public List<Book> findAll() {
        return namedParameterJdbcOperations.query("select " +
                "b.id as book_id, " +
                "b.name as book_name, " +
                "a.id as author_id, " +
                "a.fio as author_fio, " +
                "g.id as genre_id, " +
                "g.name as genre_name " +
                "from book b " +
                "inner join author a on b.author_id = a.id " +
                "inner join genre g on b.genre_id = g.id", bookMapper);
    }

    @Override
    public Optional<Book> findByName(@NonNull String name) {
        List<Book> books = namedParameterJdbcOperations.query("select " +
                        "b.id as book_id, " +
                        "b.name as book_name, " +
                        "a.id as author_id, " +
                        "a.fio as author_fio, " +
                        "g.id as genre_id, " +
                        "g.name as genre_name " +
                        "from book b " +
                        "inner join author a on b.author_id = a.id " +
                        "inner join genre g on b.genre_id = g.id where b.name = :name",
                Map.of("name", name), bookMapper);
        return books.stream().findFirst();
    }

    @Override
    public Optional<Book> findById(long id) {
        List<Book> books = namedParameterJdbcOperations.query("select " +
                        "b.id as book_id, " +
                        "b.name as book_name, " +
                        "a.id as author_id, " +
                        "a.fio as author_fio, " +
                        "g.id as genre_id, " +
                        "g.name as genre_name " +
                        "from book b " +
                        "inner join author a on b.author_id = a.id " +
                        "inner join genre g on b.genre_id = g.id where b.id = :id",
                Map.of("id", id), bookMapper);
        return books.stream().findFirst();
    }
}
