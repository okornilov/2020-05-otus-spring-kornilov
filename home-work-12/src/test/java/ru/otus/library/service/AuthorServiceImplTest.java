package ru.otus.library.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.library.domain.Author;
import ru.otus.library.repository.AuthorRepository;

import java.util.UUID;

import static org.mockito.Mockito.verify;

@DisplayName("Тестирование AuthorServiceImpl")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AuthorServiceImpl.class)
class AuthorServiceImplTest {

    @Autowired
    private AuthorService authorService;

    @MockBean
    private AuthorRepository authorRepository;

    @DisplayName("Сохранить автора")
    @Test
    void saveTest() {
        String bookId = "1";
        Author author = Author.builder().fullName("fullName").build();
        authorService.save(bookId, author);
        verify(authorRepository).save(bookId, author);
    }

    @DisplayName("Получить автора по идентификатору")
    @Test
    void findByIdTest() {
        Author author = Author.builder()
                .id(UUID.randomUUID().toString())
                .fullName("fullName").build();
        authorService.findById(author.getId());
        verify(authorRepository).findById(author.getId());
    }

    @DisplayName("Получить авторов по книге")
    @Test
    void findByBookIdTest() {
        String bookId = "1";
        Pageable pageable = PageRequest.of(0, 1);
        authorService.findByBookId(bookId, pageable);
        verify(authorRepository).findAllByBookId(bookId, pageable);
    }

    @DisplayName("Удалить автора по идентификатору")
    @Test
    void deleteById() {
        authorService.deleteById("1");
        verify(authorRepository).deleteById("1");
    }
}