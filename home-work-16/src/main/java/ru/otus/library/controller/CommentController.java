package ru.otus.library.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.library.dto.CommentDto;
import ru.otus.library.mapper.CommentMapper;
import ru.otus.library.service.CommentService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class CommentController {
    private final CommentMapper commentMapper;
    private final CommentService commentService;

    @GetMapping("/api/comments/{bookId}")
    public Page<CommentDto> list(@PathVariable String bookId, Pageable pageable) {
        return commentService.findByBookId(bookId, pageable)
                .map(commentMapper::toDto);
    }

    @PostMapping(value = "/api/comments/{bookId}")
    public ResponseEntity<CommentDto> commentSave(@PathVariable String bookId, @RequestBody @Valid CommentDto comment){
        commentService.create(bookId, commentMapper.fromDto(comment));
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @PutMapping(value = "/api/comment/{id}")
    public ResponseEntity<CommentDto> commentUpdate(@PathVariable String id, @RequestBody @Valid CommentDto comment){
        commentService.update(id, commentMapper.fromDto(comment));
        return ResponseEntity.status(HttpStatus.OK).body(comment);
    }

    @DeleteMapping("/api/comment/{id}")
    public ResponseEntity<Void> commentDelete(@PathVariable String id){
        commentService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
