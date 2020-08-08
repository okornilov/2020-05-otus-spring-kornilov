package ru.otus.library.mapper;

import org.mapstruct.Mapper;
import ru.otus.library.domain.Comment;
import ru.otus.library.dto.CommentDto;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentDto toDto(Comment comment);
    Comment fromDto(CommentDto dto);
}
