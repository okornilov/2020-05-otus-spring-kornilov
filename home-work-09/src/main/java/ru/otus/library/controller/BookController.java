package ru.otus.library.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.library.domain.Book;
import ru.otus.library.dto.BookDto;
import ru.otus.library.service.BookService;

import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class BookController {
    private final BookService bookService;

    @GetMapping("/")
    public ModelAndView list(@RequestParam(value = "page", defaultValue = "1") Integer currentPage,
                             @RequestParam(value = "size", defaultValue = "10") Integer pageSize) {

        ModelAndView modelAndView = new ModelAndView("book-list");
        Page<BookDto> page = bookService.findAll(PageRequest.of(currentPage - 1, pageSize));
        PageUtils.addPageParams(modelAndView, page);
        return modelAndView;
    }

    @GetMapping("/book")
    public String book(@RequestParam(required = false) String id, Model model){
        Book book = null;
        if (id != null) {
            book = bookService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        }
        model.addAttribute("book", Optional.ofNullable(book).orElse(new Book()));
        return "book-edit";
    }

    @PostMapping("/book")
    public String bookSave(Book book){
        bookService.save(book);
        return "redirect:/";
    }

    @PostMapping("/book-delete")
    public String bookDelete(@RequestParam String bookId){
        bookService.deleteById(bookId);
        return "redirect:/";
    }
}
