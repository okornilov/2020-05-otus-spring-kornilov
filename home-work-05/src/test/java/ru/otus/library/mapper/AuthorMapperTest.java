package ru.otus.library.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.library.domain.Author;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Тестирование AuthorMapper")
@SpringBootTest(classes = TestMapperConfiguration.class)
class AuthorMapperTest {

    @Autowired
    private AuthorMapper authorMapper;

    @DisplayName("Размапить ResultSet")
    @Test
    void mapRow() throws SQLException {
        long id = 1;
        String fio = "Ivanov";
        ResultSet rs = Mockito.mock(ResultSet.class);

        when(rs.getLong("id"))
                .thenReturn(id);
        when(rs.getString("fio"))
                .thenReturn(fio);

        Author author = authorMapper.mapRow(rs, 0);

        assertThat(author)
                .hasFieldOrPropertyWithValue("id", id)
                .hasFieldOrPropertyWithValue("fio", fio);
    }
}