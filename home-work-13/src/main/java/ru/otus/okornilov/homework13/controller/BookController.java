package ru.otus.okornilov.homework13.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.okornilov.homework13.domain.Book;
import ru.otus.okornilov.homework13.service.BookService;


@RequiredArgsConstructor
@RestController
public class BookController {

    private final BookService bookService;

    @GetMapping(value = "/api/books")
    public ResponseEntity<Iterable<Book>> books() {
        return ResponseEntity.ok(bookService.findList());
    }

    @PostMapping(value = "/api/books")
    public ResponseEntity<Book> create(@RequestBody Book book) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookService.create(book));
    }

    @PutMapping(value = "/api/books/{id}")
    public ResponseEntity<Book> update(@PathVariable Long id, @RequestBody Book book) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(bookService.update(id, book));
    }

    @DeleteMapping(value = "/api/books/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.ok().build();
    }

}
