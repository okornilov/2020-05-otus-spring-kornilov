package ru.otus.library.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Тестирование BookMapper")
@SpringBootTest(classes = TestMapperConfiguration.class)
class BookMapperTest {

    @Autowired
    private BookMapper bookMapper;

    @DisplayName("Размапить ResultSet")
    @Test
    void mapRow() throws SQLException {
        Book book = Book.builder()
                .id(1L)
                .name("book1")
                .author(Author.builder()
                        .id(2L)
                        .fio("Ivanov")
                        .build())
                .genre(Genre.builder()
                        .id(3L)
                        .name("Genre")
                        .build())
                .build();

        ResultSet rs = Mockito.mock(ResultSet.class);

        when(rs.getLong("book_id")).thenReturn(book.getId());
        when(rs.getString("book_name")).thenReturn(book.getName());
        when(rs.getLong("author_id")).thenReturn(book.getAuthor().getId());
        when(rs.getString("author_fio")).thenReturn(book.getAuthor().getFio());
        when(rs.getLong("genre_id")).thenReturn(book.getGenre().getId());
        when(rs.getString("genre_name")).thenReturn(book.getGenre().getName());

        Book book2 = bookMapper.mapRow(rs, 0);

        assertThat(book)
                .isEqualToComparingFieldByField(book2);
    }
}