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
import ru.otus.library.domain.Genre;
import ru.otus.library.service.GenreService;

import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class GenreController {
    private final GenreService genreService;

    @GetMapping("/genres")
    public ModelAndView genresList(@RequestParam String bookId,
                                   @RequestParam(value = "page", defaultValue = "1") Integer currentPage,
                                   @RequestParam(value = "size", defaultValue = "10") Integer pageSize) {
        ModelAndView modelAndView = new ModelAndView("genre-list");
        Page<Genre> page = genreService.findByBookId(bookId, PageRequest.of(currentPage - 1, pageSize));
        modelAndView.addObject("bookId", bookId);
        PageUtils.addPageParams(modelAndView, page);
        return modelAndView;
    }

    @GetMapping("/genre")
    public String genre(@RequestParam String bookId, @RequestParam(required = false) String id, Model model){
        Genre genre = null;
        if (id != null) {
            genre = genreService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        }
        model.addAttribute("genre", Optional.ofNullable(genre).orElse(new Genre()));
        model.addAttribute("bookId", bookId);
        return "genre-edit";
    }


    @PostMapping("/genre")
    public String genreSave(@RequestParam String bookId, Genre genre){
        genreService.save(bookId, genre);
        return String.format("redirect:/genres?bookId=%s", bookId);
    }

    @PostMapping("/genre-delete")
    public String genreDelete(@RequestParam String bookId, @RequestParam String id) {
        genreService.deleteById(id);
        return String.format("redirect:/genres?bookId=%s", bookId);
    }

}
