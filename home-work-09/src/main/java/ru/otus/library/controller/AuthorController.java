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
import ru.otus.library.domain.Author;
import ru.otus.library.service.AuthorService;

import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping("/authors")
    public ModelAndView authorList(@RequestParam String bookId,
                                   @RequestParam(value = "page", defaultValue = "1") Integer currentPage,
                                   @RequestParam(value = "size", defaultValue = "10") Integer pageSize) {
        ModelAndView modelAndView = new ModelAndView("author-list");
        Page<Author> page = authorService.findByBookId(bookId, PageRequest.of(currentPage - 1, pageSize));
        modelAndView.addObject("bookId", bookId);
        PageUtils.addPageParams(modelAndView, page);
        return modelAndView;
    }

    @GetMapping("/author")
    public String author(@RequestParam String bookId, @RequestParam(required = false) String id, Model model){
        Author author = null;
        if (id != null) {
            author = authorService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        }
        model.addAttribute("author", Optional.ofNullable(author).orElse(new Author()));
        model.addAttribute("bookId", bookId);
        return "author-edit";
    }


    @PostMapping("/author")
    public String authorSave(@RequestParam String bookId, Author author){
        authorService.save(bookId, author);
        return String.format("redirect:/authors?bookId=%s", bookId);
    }

    @PostMapping("/author-delete")
    public String authorDelete(@RequestParam String bookId, @RequestParam String id) {
        authorService.deleteById(id);
        return String.format("redirect:/authors?bookId=%s", bookId);
    }

}
