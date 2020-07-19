package ru.otus.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CommentDto {
    private String id;
    private String bookId;
    private String bookName;
    private LocalDateTime dateTime;
    private String text;
}
