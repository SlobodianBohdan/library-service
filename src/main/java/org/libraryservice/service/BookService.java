package org.libraryservice.service;

import org.libraryservice.entity.Book;

import java.util.List;

public interface BookService {

    List<Book> getAll();

    Book getById(Long idBook);

    Book createdBook (Book book);

    Book updateBook (Book book);

    void deleteBookById(Long idBook);

    void addBookByIdForUserById(Long idBook, Long idUser);

    void deleteBookByIdForUserById(Long idBook, Long idUser);
}
