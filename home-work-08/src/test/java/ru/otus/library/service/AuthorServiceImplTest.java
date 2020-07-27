package ru.otus.library.service;

import de.vandermeer.asciitable.AsciiTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.library.domain.Author;
import ru.otus.library.dto.AuthorDto;
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

    @DisplayName("Добавить автора")
    @Test
    void createTest() {
        String bookId = "1";
        Author author = Author.builder().fullName("fullName").build();
        authorService.create(bookId, author);
        verify(authorRepository).add(bookId, author);
    }

    @DisplayName("Изменить автора")
    @Test
    void updateTest() {
        Author author = Author.builder().id("1").fullName("fullName").build();
        authorService.update(author.getId(), author);
        verify(authorRepository).update(author.getId(), author);
    }

    @DisplayName("Показать таблицу авторов")
    @Test
    void showTable() {
        when(bundleService.getMessage("author.list")).thenReturn("author list");
        when(bundleService.getMessage("book.id")).thenReturn("book_id");
        when(bundleService.getMessage("book.name")).thenReturn("book_name");
        when(bundleService.getMessage("author.id")).thenReturn("author_id");
        when(bundleService.getMessage("author.fullName")).thenReturn("author_full_name");
        List<AuthorDto> authorList = List.of(
                AuthorDto.builder()
                        .id("1")
                        .bookId("b1")
                        .bookName("bName1")
                        .fullName("fullName1").build(),
                AuthorDto.builder()
                        .id("2")
                        .bookId("b2")
                        .bookName("bName2")
                        .fullName("fullName2").build());

        final PageRequest pageRequest = PageRequest.of(0, authorList.size());
        final PageImpl<AuthorDto> page = new PageImpl<AuthorDto>(authorList, pageRequest, authorList.size());

        when(authorRepository.findAll(pageRequest))
                .thenReturn(page);

        AsciiTable asciiTable = new AsciiTable();
        asciiTable.addRule();
        asciiTable.addRow(List.of("author_id", "book_id", "book_name", "author_full_name"));
        authorList.forEach(dto -> {
            asciiTable.addRule();
            asciiTable.addRow(dto.getId(), dto.getBookId(), dto.getBookName(), dto.getFullName());
        });
        asciiTable.addRule();

        authorService.showTable(pageRequest);
        verify(ioService).outLine("author list:");
        verify(ioService).outLine(asciiTable.render());
    }

    @DisplayName("Удалить автора по идентификатору")
    @Test
    void deleteById() {
        authorService.deleteById("1");
        verify(authorRepository).deleteById("1");
    }

    @DisplayName("Удалить всех авторов")
    @Test
    void deleteAll() {
        authorService.deleteAll();
        verify(authorRepository).deleteAll();
    }
}