package org.libraryservice.controller;

import org.libraryservice.dto.BookDto;
import org.libraryservice.mapper.BookDtoMapper;
import org.libraryservice.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/books")
public class BookController {

    private final BookService bookService;
    private final BookDtoMapper mapper;

    @Autowired
    public BookController(BookService bookService, BookDtoMapper mapper) {
        this.bookService = bookService;
        this.mapper = mapper;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookDto> getAll() {
        return mapper.toDtoList(bookService.getAll());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto create(@Validated @RequestBody BookDto bookDto) {
        return mapper.toDto(bookService.createdBook(mapper.toEntity(bookDto)));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookDto update(@Validated @RequestBody BookDto bookDto, @PathVariable Long id) {
        bookDto.setId(id);
        return mapper.toDto(bookService.updateBook(mapper.toEntity(bookDto)));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookDto getById(@PathVariable Long id) {
        return mapper.toDto(bookService.getById(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBookById(id);
    }

    @PutMapping("/take-book/{idBook}")
    @ResponseStatus(HttpStatus.OK)
    public void takeBook(@PathVariable Long idBook, @RequestParam Long idUser) {
        bookService.addBookByIdForUserById(idBook, idUser);
    }

    @PutMapping("/return-book/{idBook}")
    @ResponseStatus(HttpStatus.OK)
    public void returnBook(@PathVariable Long idBook, @RequestParam Long idUser) {
        bookService.deleteBookByIdForUserById(idBook, idUser);
    }
}
