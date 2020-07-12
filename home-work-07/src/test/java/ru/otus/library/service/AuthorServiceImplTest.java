package ru.otus.library.service;

import de.vandermeer.asciitable.AsciiTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.library.domain.Author;
import ru.otus.library.repository.AuthorRepository;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Тестирование AuthorServiceImpl")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AuthorServiceImpl.class)
class AuthorServiceImplTest {

    @Autowired
    private AuthorService authorService;

    @MockBean
    private IOService ioService;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private MessageBundleService bundleService;

    @DisplayName("Сохранить автора")
    @Test
    void save() {
        Author author = Author.builder().id(1L).fullName("fullName").build();
        authorService.save(author);
        verify(authorRepository).save(author);
    }

    @DisplayName("Показать таблицу авторов")
    @Test
    void showTable() {
        when(bundleService.getMessage("author.list")).thenReturn("author list");
        when(bundleService.getMessage("author.id")).thenReturn("author_id");
        when(bundleService.getMessage("author.fullName")).thenReturn("author_full_name");
        List<Author> authorList = List.of(
                Author.builder().id(1L).fullName("fullName1").build(),
                Author.builder().id(2L).fullName("fullName2").build());
        when(authorRepository.findAll()).thenReturn(authorList);

        AsciiTable asciiTable = new AsciiTable();
        asciiTable.addRule();
        asciiTable.addRow(List.of("author_id", "author_full_name"));
        authorList.forEach(author -> {
            asciiTable.addRule();
            asciiTable.addRow(author.getId(), author.getFullName());
        });
        asciiTable.addRule();

        authorService.showTable();
        verify(ioService).outLine("author list:");
        verify(ioService).outLine(asciiTable.render());
    }

    @DisplayName("Удлаить автора по идентификатору")
    @Test
    void deleteById() {
        authorService.deleteById(1L);
        verify(authorRepository).deleteById(1L);
    }

    @DisplayName("Удалить всех авторов")
    @Test
    void deleteAll() {
        authorService.deleteAll();
        verify(authorRepository).deleteAll();
    }
}