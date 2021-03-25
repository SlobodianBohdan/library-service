package org.libraryservice.unit.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.libraryservice.entity.Book;
import org.libraryservice.entity.User;
import org.libraryservice.repository.BookRepository;
import org.libraryservice.repository.UserRepository;
import org.libraryservice.service.BookService;
import org.libraryservice.service.impl.BookServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@WebMvcTest(value = BookService.class)
public class BookServiceTest {


    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private BookServiceImpl bookService;

    private Book book1;
    private Book book2;
    private Book book3;
    private User user;


    @BeforeEach
    public void beforeEach() {

        //Create test data
        book1 = Book.builder().id(1L).name("Point of Deception").isFree(true).build();
        book2 = Book.builder().id(2L).name("Blue skye").isFree(true).build();
        book3 = Book.builder().id(3L).name("Window in future").isFree(true).build();
        user = User.builder().id(1L).name("Den").build();

    }

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(bookRepository, userRepository);
    }

    @Test
    public void checkAllBooks_successFlow() {
        List<Book> expected = Arrays.asList(book1, book2, book3);

        when(bookRepository.findAll()).thenReturn(expected);
        List<Book> actual = bookService.getAll();

        assertEquals(expected, actual);
        verify(bookRepository).findAll();
    }

    @Test
    public void checkBookById_successFlow() {
        Book expected = Book.builder().id(1L).name("Point of Deception").isFree(true).build();

        when(bookRepository.findById(expected.getId())).thenReturn(java.util.Optional.of(expected));
        Book actual = bookService.getById(expected.getId());

        assertEquals(expected, actual);
        verify(bookRepository).findById(expected.getId());
    }

    @Test
    public void checkCreateBook_successFlow() {
        Book expected = book1;

        when(bookRepository.save(book1)).thenReturn(expected);
        Book actual = bookService.creatBook(book1);

        assertEquals(expected, actual);
        verify(bookRepository).save(book1);
    }

    @Test
    public void checkUpdateBook_successFlow() {
        Book expected = book1;

        when(bookRepository.save(book1)).thenReturn(expected);
        when(bookRepository.findById(book1.getId())).thenReturn(Optional.of(book1));
        Book actual = bookService.updateBook(book1);

        assertEquals(expected, actual);
        verify(bookRepository).save(book1);
        verify(bookRepository).findById(book1.getId());
    }

    @Test
    public void checkDeleteBookById_successFlow() {
        when(bookRepository.findById(book1.getId())).thenReturn(Optional.of(book1));

        bookService.deleteBookById(book1.getId());

        verify(bookRepository).findById(book1.getId());
        verify(bookRepository).deleteById(book1.getId());
    }

    @Test
    public void checkTakeBookById_successFlow() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        when(bookRepository.findBookByIdAndIsFreeIsTrue(book1.getId())).thenReturn(book1);

        bookService.takeBook(book1.getId(), user.getId());

        verify(userRepository).findById(user.getId());
        verify(bookRepository).findBookByIdAndIsFreeIsTrue(book1.getId());
        verify(bookRepository).save(book1);
    }

    @Test
    public void checkReturnBookById_successFlow() {
        book1.setFree(false);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(bookRepository.findBookByIdAndIsFreeIsFalseAndUser(book1.getId(), user)).thenReturn(book1);

        bookService.returnBook(book1.getId(), user.getId());

        verify(userRepository).findById(user.getId());
        verify(bookRepository).findBookByIdAndIsFreeIsFalseAndUser(book1.getId(), user);
        verify(bookRepository).save(book1);
    }

}
