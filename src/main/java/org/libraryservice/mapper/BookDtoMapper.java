package org.libraryservice.mapper;

import org.libraryservice.dto.BookDto;
import org.libraryservice.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class BookDtoMapper implements EntityDtoMapper<Book, BookDto> {
    public BookDto toDto(Book book) {
        return BookDto.builder()
                .id(book.getId())
                .name(book.getName())
                .isFree(book.isFree())
                .build();
    }

    public Book toEntity(BookDto book) {
        return Book.builder()
                .name(book.getName())
                .isFree(book.isFree())
                .build();
    }
}
