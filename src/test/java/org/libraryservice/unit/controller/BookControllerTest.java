package org.libraryservice.unit.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.libraryservice.controller.BookController;
import org.libraryservice.dto.BookDto;
import org.libraryservice.entity.Book;
import org.libraryservice.entity.User;
import org.libraryservice.exception.EntityNotFoundException;
import org.libraryservice.mapper.BookDtoMapper;
import org.libraryservice.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = BookController.class)
public class BookControllerTest extends AbstractTest {

    @MockBean
    private BookService bookService;
    @MockBean
    private BookDtoMapper mapper;


    @Autowired
    private MockMvc mockMvc;

    private String url = "/api/books";

    private static final Book TEST_BOOK;
    private static final BookDto TEST_BOOK_DTO;
    private static final Book TEST_BOOK_FOR_CREATE;
    private static final BookDto TEST_BOOK_DTO_FOR_CREATE;
    private static final User TEST_USER;
    private static final Long FAKE_ID = Long.valueOf(Integer.MAX_VALUE);

    static {
        TEST_BOOK = Book.builder().id(1L).name("Point of Deception").isFree(true).user(null).build();
        TEST_BOOK_DTO = BookDto.builder().id(1L).name("Point of Deception").isFree(true).build();
        TEST_BOOK_FOR_CREATE = Book.builder().name("Window in future").build();
        TEST_BOOK_DTO_FOR_CREATE = BookDto.builder().name("Window in future").build();
        TEST_USER = User.builder().id(1L).name("Ihor").build();

    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(bookService, mapper);
    }

    @SneakyThrows
    @Test
    public void checkAllBooks_successFlow() {
        when(bookService.getAll()).thenReturn(List.of(TEST_BOOK));
        when(mapper.toDtoList(List.of(TEST_BOOK))).thenReturn(List.of(TEST_BOOK_DTO));

        mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect((jsonPath("$[0].id").value(TEST_BOOK.getId())))
                .andExpect(jsonPath("$[0].name").value(TEST_BOOK.getName()));

        verify(bookService).getAll();
        verify(mapper).toDtoList(List.of(TEST_BOOK));
    }

    @SneakyThrows
    @Test
    public void checkAllBooks_errorFlow() {
        when(bookService.getAll()).thenThrow(new EntityNotFoundException("Books isn't found!"));

        mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Books isn't found!"));

        verify(bookService).getAll();
    }


    @SneakyThrows
    @Test
    public void checkBookById_successFlow() {
        when(bookService.getById(TEST_BOOK.getId())).thenReturn(TEST_BOOK);
        when(mapper.toDto(TEST_BOOK)).thenReturn(TEST_BOOK_DTO);

        mockMvc.perform(get(url + "/" + TEST_BOOK.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.id").value(TEST_BOOK.getId())))
                .andExpect(jsonPath("$.name").value(TEST_BOOK.getName()))
                .andExpect(jsonPath("$.free").value(TEST_BOOK.isFree()));


        verify(bookService).getById(TEST_BOOK.getId());
        verify(mapper).toDto(TEST_BOOK);
    }

    @SneakyThrows
    @Test
    public void checkBookById_errorFlow() {
        when(bookService.getById(FAKE_ID)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get(url + "/" + FAKE_ID).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(bookService).getById(FAKE_ID);
    }

    @SneakyThrows
    @Test
    public void checkCreateBook_successFlow() {
        when(bookService.creatBook(TEST_BOOK_FOR_CREATE)).thenReturn(TEST_BOOK_FOR_CREATE);
        when(mapper.toDto(TEST_BOOK_FOR_CREATE)).thenReturn(TEST_BOOK_DTO_FOR_CREATE);
        when(mapper.toEntity(TEST_BOOK_DTO_FOR_CREATE)).thenReturn(TEST_BOOK_FOR_CREATE);


        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(TEST_BOOK_FOR_CREATE)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(TEST_BOOK_FOR_CREATE.getName()));

        verify(bookService).creatBook(TEST_BOOK_FOR_CREATE);
        verify(mapper).toDto(TEST_BOOK_FOR_CREATE);
        verify(mapper).toEntity(TEST_BOOK_DTO_FOR_CREATE);
    }

    @SneakyThrows
    @Test
    public void checkCreateBook_errorFlow() {
        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(new BookDto())))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    public void checkUpdateBook_successFlow() {
        when(bookService.updateBook(TEST_BOOK)).thenReturn(TEST_BOOK);
        when(mapper.toDto(TEST_BOOK)).thenReturn(TEST_BOOK_DTO);
        when(mapper.toEntity(TEST_BOOK_DTO)).thenReturn(TEST_BOOK);


        mockMvc.perform(put(url + "/" + TEST_BOOK.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(TEST_BOOK_DTO)))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.id").value(TEST_BOOK.getId())))
                .andExpect(jsonPath("$.name").value(TEST_BOOK.getName()));

        verify(bookService).updateBook(TEST_BOOK);
        verify(mapper).toDto(TEST_BOOK);
        verify(mapper).toEntity(TEST_BOOK_DTO);
    }

    @SneakyThrows
    @Test
    public void checkUpdateBook_errorFlow() {
        mockMvc.perform(put(url + "/" + FAKE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(new BookDto())))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    public void checkDeleteBook_successFlow() {
        doNothing().when(bookService).deleteBookById(TEST_BOOK.getId());

        mockMvc.perform(delete(url + "/" + TEST_BOOK.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        verify(bookService).deleteBookById(TEST_BOOK.getId());
    }

    @SneakyThrows
    @Test
    public void checkDeleteBook_errorFlow() {
        doThrow(new EntityNotFoundException("Book with a specified id isn't found!")).when(bookService).deleteBookById(FAKE_ID);

        mockMvc.perform(delete(url + "/" + FAKE_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book with a specified id isn't found!"));

        verify(bookService).deleteBookById(FAKE_ID);
    }

    @SneakyThrows
    @Test
    public void checkTakeBook_successFlow() {
        when(bookService.takeBook(TEST_BOOK.getId(), TEST_USER.getId())).thenReturn(true);

        mockMvc.perform(put(url + "/take-book/" + TEST_BOOK.getId() + "?idUser=" + TEST_USER.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bookService).takeBook(TEST_BOOK.getId(), TEST_USER.getId());
    }

    @SneakyThrows
    @Test
    public void checkTakeBook_errorFlow() {
        when(bookService.takeBook(FAKE_ID, FAKE_ID))
                .thenThrow(new EntityNotFoundException("Book with a specified id isn't found!")).thenReturn(false);

        mockMvc.perform(put(url + "/take-book/" + FAKE_ID + "?idUser=" + FAKE_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book with a specified id isn't found!"));

        verify(bookService).takeBook(FAKE_ID, FAKE_ID);
    }

    @SneakyThrows
    @Test
    public void checkReturnBook_successFlow() {
        doNothing().when(bookService).returnBook(TEST_BOOK.getId(), TEST_USER.getId());

        mockMvc.perform(put(url + "/return-book/" + TEST_BOOK.getId() + "?idUser=" + TEST_USER.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bookService).returnBook(TEST_BOOK.getId(), TEST_USER.getId());
    }

    @SneakyThrows
    @Test
    public void checkReturnBook_errorFlow() {
        doThrow(new EntityNotFoundException("Book with a specified id isn't found!")).when(bookService).returnBook(FAKE_ID, FAKE_ID);

        mockMvc.perform(put(url + "/return-book/" + FAKE_ID + "?idUser=" + FAKE_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book with a specified id isn't found!"));

        verify(bookService).returnBook(FAKE_ID, FAKE_ID);
    }
}
