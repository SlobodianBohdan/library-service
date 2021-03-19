package org.libraryservice.service.impl;

import org.libraryservice.entity.Book;
import org.libraryservice.exception.BookDuplicateException;
import org.libraryservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.libraryservice.repository.BookRepository;
import org.libraryservice.service.BookService;

import java.util.List;

// implement main logic and logs here
@Service
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;
    private UserRepository userRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book getById(Long idBook) {
        return bookRepository.findBookById(idBook);
    }

    @Override
    public Book createdBook(Book book) {
        try {
            return bookRepository.save(book);
        } catch (DataIntegrityViolationException e) {
            throw new BookDuplicateException("Book with a specified name exists!");
        }
    }

    @Override
    public Book updateBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public void deleteBookById(Long idBook) {
        bookRepository.deleteById(idBook);
    }

    @Override
    public void addBookByIdForUserById(Long idBook, Long idUser) {
        Book addBook = bookRepository.findBookByIdAndIsFreeIsTrue(idBook);
        if (addBook != null){
            addBook.setUser(userRepository.findAllById(idUser));
            addBook.setFree(false);
            bookRepository.save(addBook);
        }
    }

    @Override
    public void deleteBookByIdForUserById(Long idBook, Long idUser) {
        Book addBook = bookRepository.findBookByIdAndIsFreeIsFalse(idBook);
        if (addBook != null){
            addBook.setUser(null);
            addBook.setFree(true);
            bookRepository.save(addBook);
        }
    }
}
