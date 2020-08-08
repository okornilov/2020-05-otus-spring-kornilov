package ru.otus.library.mapper;

import org.mapstruct.Mapper;
import ru.otus.library.domain.Book;
import ru.otus.library.dto.BookDto;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookDto toDto(Book book);
    Book fromDto(BookDto dto);
}
