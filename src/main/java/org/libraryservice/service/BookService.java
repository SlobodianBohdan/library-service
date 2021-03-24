package org.libraryservice.service;

import org.libraryservice.entity.Book;

import java.util.List;

public interface BookService {

    List<Book> getAll();

    Book getById(Long idBook);

    Book creatBook (Book book);

    Book updateBook (Book book);

    void deleteBookById(Long idBook);

    boolean takeBook(Long idBook, Long idUser);

    void returnBook(Long idBook, Long idUser);
}
