package ru.otus.library.service;

import de.vandermeer.asciitable.AsciiTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.library.dao.AuthorDao;
import ru.otus.library.domain.Author;

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
    private AuthorDao authorDao;

    @MockBean
    private MessageBundleService bundleService;

    @DisplayName("Сохранить автора")
    @Test
    void save() {
        Author author = Author.builder().id(1L).fio("fio").build();
        authorService.save(author);
        verify(authorDao).save(author);
    }

    @DisplayName("Показать таблицу авторов")
    @Test
    void showTable() {
        when(bundleService.getMessage("author.list")).thenReturn("author list");
        when(bundleService.getMessage("author.id")).thenReturn("author_id");
        when(bundleService.getMessage("author.fio")).thenReturn("author_fio");
        List<Author> authorList = List.of(
                Author.builder().id(1L).fio("fio1").build(),
                Author.builder().id(2L).fio("fio2").build());
        when(authorDao.findAll()).thenReturn(authorList);

        AsciiTable asciiTable = new AsciiTable();
        asciiTable.addRule();
        asciiTable.addRow(List.of("author_id", "author_fio"));
        authorList.forEach(author -> {
            asciiTable.addRule();
            asciiTable.addRow(author.getId(), author.getFio());
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
        verify(authorDao).deleteById(1L);
    }

    @DisplayName("Удалить всех авторов")
    @Test
    void deleteAll() {
        authorService.deleteAll();
        verify(authorDao).deleteAll();
    }
}