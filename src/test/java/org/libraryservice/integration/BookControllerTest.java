package org.libraryservice.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.libraryservice.LibraryServiceApplication;
import org.libraryservice.dto.BookDto;
import org.libraryservice.dto.ResponseMessageDto;
import org.libraryservice.dto.UserDto;
import org.libraryservice.entity.Book;
import org.libraryservice.entity.User;
import org.libraryservice.mapper.BookDtoMapper;
import org.libraryservice.mapper.UserDtoMapper;
import org.libraryservice.repository.BookRepository;
import org.libraryservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = LibraryServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest extends AbstractContainer{

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestRestTemplate template;
    @Autowired
    private BookDtoMapper bookDtoMapper;
    @Autowired
    private UserDtoMapper userDtoMapper;

    private HttpHeaders headers = new HttpHeaders();
    private HttpEntity<BookDto> request;

    private final String exceptionMessage = "Book with a specified id isn't found!";
    private BookDto TEST_BOOK_DTO_TO_CREATE, TEST_BOOK_DTO;
    private UserDto TEST_USER_DTO_TO_CREATE;

    @BeforeEach
    void beforeEach() {
        TEST_BOOK_DTO_TO_CREATE = BookDto.builder().name("test name 1").isFree(true).build();
        TEST_BOOK_DTO = BookDto.builder().id(1L).name("test name 2").isFree(true).build();
        TEST_USER_DTO_TO_CREATE = UserDto.builder().name("Test user name").build();
    }


    @AfterEach
    void afterEach() {
        bookRepository.deleteAll();
    }


    @Test
    void createBookTest_successFlow() {
        final String url = "/api/books";
        BookDto requestDto = TEST_BOOK_DTO_TO_CREATE;
        request = new HttpEntity<>(requestDto, headers);

        //Make call
        ResponseEntity<BookDto> response =
                this.template.exchange(url, HttpMethod.POST, request, BookDto.class);

        //Verify request succeed
        assertEquals(201, response.getStatusCodeValue());
        assertThat(requestDto).isEqualToIgnoringGivenFields(response.getBody(), "id");
    }

    @Test
    void createDuplicateBookTest_unSuccessFlow() {
        final String url = "/api/books";
        BookDto requestDto = TEST_BOOK_DTO_TO_CREATE;
        bookRepository.save(bookDtoMapper.toEntity(TEST_BOOK_DTO_TO_CREATE));

        //Make call
        request = new HttpEntity<>(requestDto, headers);
        ResponseEntity<ResponseMessageDto> response = this.template.exchange(
                url, HttpMethod.POST, request, ResponseMessageDto.class);

        //Verify BookNotFoundException exception
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Book with a specified name exists!", response.getBody().getMessage());
    }

    @Test
    void createBookTestWithMissedRequiredField_unSuccessFlow() {
        final String url = "/api/books";
        //Make field name empty
        BookDto requestDto = TEST_BOOK_DTO_TO_CREATE;
        requestDto.setName(" ");
        request = new HttpEntity<>(requestDto, headers);

        //Make call
        ResponseEntity<ResponseMessageDto> response =
                this.template.exchange(url, HttpMethod.POST, request, ResponseMessageDto.class);

        //Verify this exception because of validation missed field
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void updateBookTest_successFlow() {
        BookDto requestDto = TEST_BOOK_DTO;
        Long id = bookRepository.save(bookDtoMapper.toEntity(requestDto)).getId();


        //Make call
        requestDto.setName("new book name");
        final String url = "/api/books/" + id;
        request = new HttpEntity<>(requestDto, headers);
        ResponseEntity<BookDto> response =
                this.template.exchange(url, HttpMethod.PUT, request, BookDto.class);

        //Verify request succeed
        assertEquals(200, response.getStatusCodeValue());
        assertThat(requestDto).isEqualTo(response.getBody());
    }

    @Test
    void updateBookTest_unSuccessFlow() {
        BookDto requestDto = TEST_BOOK_DTO;

        //Make call with fake animalId
        requestDto.setName("new book name");
        final String url = "/api/books/" + Integer.MAX_VALUE;
        request = new HttpEntity<>(requestDto, headers);
        ResponseEntity<ResponseMessageDto> response = this.template.exchange(
                url, HttpMethod.PUT, request, ResponseMessageDto.class);

        //Verify BookNotFoundException exception
        assertEquals(404, response.getStatusCodeValue());
        assertEquals(exceptionMessage, response.getBody().getMessage());
    }

    @Test
    void getBookByIdTest_successFlow() {
        BookDto requestDto = TEST_BOOK_DTO;
        Long bookId = bookRepository.save(bookDtoMapper.toEntity(requestDto)).getId();

        //Make call
        final String url = "/api/books/" + bookId;
        ResponseEntity<BookDto> response =
                this.template.exchange(url, HttpMethod.GET, null, BookDto.class);

        //Verify request succeed
        assertEquals(200, response.getStatusCodeValue());
        assertThat(requestDto).isEqualToIgnoringGivenFields(response.getBody(), "id");
    }


    @Test
    void getBookByIdTest_unSuccessFlow() {
        //Make call
        final String url = "/api/books/" + Integer.MAX_VALUE;
        ResponseEntity<ResponseMessageDto> response = this.template.exchange(
                url, HttpMethod.GET, null,
                ResponseMessageDto.class);

        //Verify BookNotFoundException exception
        assertEquals(404, response.getStatusCodeValue());
        assertEquals(exceptionMessage, response.getBody().getMessage());
    }

    @Test
    void deleteBook_successFlow() {
        BookDto requestDto = TEST_BOOK_DTO;
        Long id = bookRepository.save(bookDtoMapper.toEntity(requestDto)).getId();

        final String url = "/api/books/" + id;

        ResponseEntity<ResponseEntity> response
                = template.exchange(url, HttpMethod.DELETE, null, ResponseEntity.class);

        //Checking if entity was deleted
        assertEquals(204, response.getStatusCodeValue());

    }

    @Test
    void takeBookTest_successFlow() {
        //Preparing data
        Long bookId = bookRepository.save(bookDtoMapper.toEntity(TEST_BOOK_DTO_TO_CREATE)).getId();
        Long userId = userRepository.save(userDtoMapper.toEntity(TEST_USER_DTO_TO_CREATE)).getId();

        //Make call
        final String url = "/api/books/take-book/" + bookId + "?idUser=" + userId;
        ResponseEntity<ResponseEntity> response =
                this.template.exchange(url, HttpMethod.PUT, null, ResponseEntity.class);

        //Verify request succeed
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void takeBookWishFalseFreeStatusTest_unSuccessFlow() {
        //Preparing data
        TEST_BOOK_DTO_TO_CREATE.setFree(false);
        Long bookId = bookRepository.save(bookDtoMapper.toEntity(TEST_BOOK_DTO_TO_CREATE)).getId();
        Long userId = userRepository.save(userDtoMapper.toEntity(TEST_USER_DTO_TO_CREATE)).getId();

        //Make call
        final String url = "/api/books/take-book/" + bookId + "?idUser=" + userId;
        ResponseEntity<ResponseMessageDto> response =
                this.template.exchange(url, HttpMethod.PUT, null, ResponseMessageDto.class);

        //Verify BookNotFoundException exception
        assertEquals(404, response.getStatusCodeValue());
        assertEquals(exceptionMessage, response.getBody().getMessage());
    }

    @Test
    void returnBookTest_successFlow() {
        //Preparing data
        Book bookToCreate = bookDtoMapper.toEntity(TEST_BOOK_DTO_TO_CREATE);
        User userToCreate = userDtoMapper.toEntity(TEST_USER_DTO_TO_CREATE);
        bookToCreate.setUser(userToCreate);
        bookToCreate.setFree(false);
        Long userId = userRepository.save(userToCreate).getId();
        Long bookId = bookRepository.save(bookToCreate).getId();


        //Make call
        final String url = "/api/books/return-book/" + bookId + "?idUser=" + userId;
        ResponseEntity<ResponseEntity> response =
                this.template.exchange(url, HttpMethod.PUT, null, ResponseEntity.class);

        //Verify request succeed
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void returnBookWithWrongUserThatDoesNotHaveBookTest_unSuccessFlow() {
        //Preparing data
        Book bookToCreate = bookDtoMapper.toEntity(TEST_BOOK_DTO_TO_CREATE);
        User userThatDoesNotHaveBook = userDtoMapper.toEntity(TEST_USER_DTO_TO_CREATE);
        User userToCreate = userDtoMapper.toEntity(TEST_USER_DTO_TO_CREATE);
        bookToCreate.setUser(userToCreate);
        bookToCreate.setFree(false);
        Long userIdThatDoesNotHaveBook = userRepository.save(userThatDoesNotHaveBook).getId();
        userRepository.save(userToCreate).getId();
        Long bookId = bookRepository.save(bookToCreate).getId();


        //Make call
        final String url = "/api/books/return-book/" + bookId + "?idUser=" + userIdThatDoesNotHaveBook;
        ResponseEntity<ResponseMessageDto> response =
                this.template.exchange(url, HttpMethod.PUT, null, ResponseMessageDto.class);

        //Verify BookNotFoundException exception
        assertEquals(404, response.getStatusCodeValue());
        assertEquals(exceptionMessage, response.getBody().getMessage());
    }


}
