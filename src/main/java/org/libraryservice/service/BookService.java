package org.libraryservice.service;

import org.libraryservice.entity.Book;

import java.util.List;

public interface BookService {

    List<Book> getAll();

    Book createdBook (Book book);

    Book updateBook (Book book);

    void deleteBookById(Long idBook);
}
