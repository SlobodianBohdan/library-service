package org.libraryservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.libraryservice.entity.Book;
import org.libraryservice.exception.EntityDuplicateException;
import org.libraryservice.exception.EntityNotFoundException;
import org.libraryservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.libraryservice.repository.BookRepository;
import org.libraryservice.service.BookService;

import java.util.List;

@Slf4j
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
        findByIdOrThrowException(bookRepository, idBook);
        return bookRepository.findBookById(idBook);
    }

    @Override
    public Book createdBook(Book book) {
        try {
            book.setFree(true);
            return bookRepository.save(book);
        } catch (DataIntegrityViolationException e) {
            throw new EntityDuplicateException("Book with a specified name exists!");
        }
    }

    @Override
    public Book updateBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public void deleteBookById(Long idBook) {
        findByIdOrThrowException(bookRepository, idBook);
        bookRepository.deleteById(idBook);
    }

    @Override
    public void addBookByIdForUserById(Long idBook, Long idUser) {
        findByIdOrThrowException(userRepository, idUser);
        findByIdOrThrowException(bookRepository, idBook);
        Book addBook = bookRepository.findBookByIdAndIsFreeIsTrue(idBook);
        if (addBook != null){
            addBook.setUser(userRepository.findAllById(idUser));
            addBook.setFree(false);
            bookRepository.save(addBook);
        }
    }

    @Override
    public void deleteBookByIdForUserById(Long idBook, Long idUser) {
        findByIdOrThrowException(userRepository, idUser);
        findByIdOrThrowException(bookRepository, idBook);
        Book addBook = bookRepository.findBookByIdAndIsFreeIsFalse(idBook);
        if (addBook != null){
            addBook.setUser(null);
            addBook.setFree(true);
            bookRepository.save(addBook);
        }
    }

    private <T> T findByIdOrThrowException(JpaRepository<T, Long> repository, Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity is not found!"));
    }
}
