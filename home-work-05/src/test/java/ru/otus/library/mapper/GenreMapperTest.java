package ru.otus.library.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.library.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Тестирование GenreMapper")
@SpringBootTest(classes = TestMapperConfiguration.class)
class GenreMapperTest {

    @Autowired
    private GenreMapper genreMapper;

    @DisplayName("Размапить ResultSet")
    @Test
    void mapRow() throws SQLException {
        long id = 1;
        String name = "Genre";
        ResultSet rs = Mockito.mock(ResultSet.class);

        when(rs.getLong("id"))
                .thenReturn(id);
        when(rs.getString("name"))
                .thenReturn(name);

        Genre genre = genreMapper.mapRow(rs, 0);

        assertThat(genre)
                .hasFieldOrPropertyWithValue("id", id)
                .hasFieldOrPropertyWithValue("name", name);
    }
}