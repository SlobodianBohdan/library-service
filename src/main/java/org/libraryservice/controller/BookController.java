package org.libraryservice.controller;

import org.libraryservice.dto.BookDto;
import org.libraryservice.mapper.BookDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.libraryservice.service.BookService;

@RestController
@RequestMapping("api/books")
public class BookController {

    private BookService bookService;
    private BookDtoMapper mapper;

    @Autowired
    public BookController(BookService bookService, BookDtoMapper mapper) {
        this.bookService = bookService;
        this.mapper = mapper;
    }

    @PutMapping("/{adminId}")
    @ResponseStatus(HttpStatus.OK)
    public BookDto update(@Validated @RequestBody BookDto bookDto, @PathVariable Long bookId){
        bookDto.setId(bookId);
        return mapper.toDto(bookService.updateBook(mapper.toEntity(bookDto)));
    }
}
