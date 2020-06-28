package ru.otus.library.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class BookMapper implements RowMapper<Book> {
    @Override
    public Book mapRow(ResultSet resultSet, int i) throws SQLException {
        return Book.builder()
                .id(resultSet.getLong("book_id"))
                .name(resultSet.getString("book_name"))
                .author(Author.builder()
                        .id(resultSet.getLong("author_id"))
                        .fio(resultSet.getString("author_fio"))
                        .build())
                .genre(Genre.builder()
                        .id(resultSet.getLong("genre_id"))
                        .name(resultSet.getString("genre_name"))
                        .build())
                .build();
    }
}
