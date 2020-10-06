package ru.otus.library.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.otus.library.domain.Book;
import ru.otus.library.dto.BookDto;
import ru.otus.library.mapper.BookMapper;
import ru.otus.library.service.BookService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class BookController {
    private final BookMapper bookMapper;
    private final BookService bookService;

    @GetMapping("/api/books")
    public Page<BookDto> list(Pageable pageable) {
        return bookService.findAll(pageable)
                .map(bookMapper::toDto);
    }

    @GetMapping("/api/book/{id}")
    public BookDto book(@PathVariable String id){
        return bookService.findById(id)
                .map(bookMapper::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/api/book/{id}")
    public ResponseEntity<BookDto> bookUpdate(@PathVariable String id, @RequestBody @Valid BookDto book){
        book.setId(id);
        Book savedBook = bookService.save(bookMapper.fromDto(book));
        return ResponseEntity.status(HttpStatus.OK).body(bookMapper.toDto(savedBook));
    }

    @PostMapping("/api/book")
    public ResponseEntity<BookDto> bookSave(@RequestBody @Valid BookDto book){
        Book savedBook = bookService.save(bookMapper.fromDto(book));
        return ResponseEntity.status(HttpStatus.CREATED).body(bookMapper.toDto(savedBook));
    }

    @DeleteMapping("/api/book/{id}")
    public ResponseEntity<Void> bookDelete(@PathVariable String id){
        bookService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
