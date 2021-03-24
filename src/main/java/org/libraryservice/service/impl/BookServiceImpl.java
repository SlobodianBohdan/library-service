package org.libraryservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.libraryservice.entity.Book;
import org.libraryservice.entity.User;
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
import java.util.Optional;

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
        List<Book> listBooks = bookRepository.findAll();
        if (listBooks.isEmpty()){
            throw new EntityNotFoundException("Books isn't found!");
        }
        return listBooks;
    }

    @Override
    public Book getById(Long idBook) {
        return findByIdOrThrowException(bookRepository, idBook);
    }

    @Override
    public Book creatBook(Book book) {
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
    public boolean takeBook(Long idBook, Long idUser) {
        User foundUser = findByIdOrThrowException(userRepository, idUser);
        Book addBook = Optional.ofNullable(bookRepository.findBookByIdAndIsFreeIsTrue(idBook))
                .orElseThrow(() -> new EntityNotFoundException("Book with a specified id isn't found!"));

        if (addBook != null){
            addBook.setUser(foundUser);
            addBook.setFree(false);
            bookRepository.save(addBook);
            return true;
        }

        return false;
    }

    @Override
    public void returnBook(Long idBook, Long idUser) {
        findByIdOrThrowException(userRepository, idUser);
        Book addBook = Optional.ofNullable(bookRepository.findBookByIdAndIsFreeIsFalse(idBook))
                .orElseThrow(() -> new EntityNotFoundException("Book with a specified id isn't found!"));

        if (addBook != null){
            addBook.setUser(null);
            addBook.setFree(true);
            bookRepository.save(addBook);
        }
    }

    private <T> T findByIdOrThrowException(JpaRepository<T, Long> repository, Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with a specified id isn't found!"));
    }
}
