package org.libraryservice.unit.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.libraryservice.controller.UserController;
import org.libraryservice.dto.UserDto;
import org.libraryservice.entity.User;
import org.libraryservice.exception.EntityNotFoundException;
import org.libraryservice.mapper.UserDtoMapper;
import org.libraryservice.service.UserService;
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

@WebMvcTest(value = UserController.class)
public class UserControllerTest extends AbstractTest {

    @MockBean
    private UserService userService;
    @MockBean
    private UserDtoMapper mapper;


    @Autowired
    private MockMvc mockMvc;

    private String url = "/api/users";

    private static final User TEST_USER;
    private static final UserDto TEST_USER_DTO;
    private static final User TEST_USER_FOR_CREATE;
    private static final UserDto TEST_USER_DTO_FOR_CREATE;
    private static final Long FAKE_ID = Long.valueOf(Integer.MAX_VALUE);

    static {
        TEST_USER = User.builder().id(1L).name("Ihor").build();
        TEST_USER_DTO = UserDto.builder().id(1L).name("Ihor").build();
        TEST_USER_FOR_CREATE = User.builder().name("Ivan").build();
        TEST_USER_DTO_FOR_CREATE = UserDto.builder().name("Ivan").build();
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(userService, mapper);
    }

    @SneakyThrows
    @Test
    public void checkAllUsers_successFlow() {
        when(userService.getAll()).thenReturn(List.of(TEST_USER));
        when(mapper.toDtoList(List.of(TEST_USER))).thenReturn(List.of(TEST_USER_DTO));

        mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect((jsonPath("$[0].id").value(TEST_USER.getId())))
                .andExpect(jsonPath("$[0].name").value(TEST_USER.getName()));

        verify(userService).getAll();
        verify(mapper).toDtoList(List.of(TEST_USER));
    }

    @SneakyThrows
    @Test
    public void checkAllUsers_errorFlow() {
        when(userService.getAll()).thenThrow(new EntityNotFoundException("Users isn't found!"));

        mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Users isn't found!"));

        verify(userService).getAll();
    }

    @SneakyThrows
    @Test
    public void checkUserById_successFlow() {
        when(userService.getById(TEST_USER.getId())).thenReturn(TEST_USER);
        when(mapper.toDto(TEST_USER)).thenReturn(TEST_USER_DTO);

        mockMvc.perform(get(url + "/" + TEST_USER.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.id").value(TEST_USER.getId())))
                .andExpect(jsonPath("$.name").value(TEST_USER.getName()));


        verify(userService).getById(TEST_USER.getId());
        verify(mapper).toDto(TEST_USER);
    }

    @SneakyThrows
    @Test
    public void checkUserById_errorFlow() {
        when(userService.getById(FAKE_ID)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get(url + "/" + FAKE_ID).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(userService).getById(FAKE_ID);
    }

    @SneakyThrows
    @Test
    public void checkCreateUser_successFlow() {
        when(userService.creatUser(TEST_USER_FOR_CREATE)).thenReturn(TEST_USER_FOR_CREATE);
        when(mapper.toDto(TEST_USER_FOR_CREATE)).thenReturn(TEST_USER_DTO_FOR_CREATE);
        when(mapper.toEntity(TEST_USER_DTO_FOR_CREATE)).thenReturn(TEST_USER_FOR_CREATE);


        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(TEST_USER_DTO_FOR_CREATE)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(TEST_USER_FOR_CREATE.getName()));

        verify(userService).creatUser(TEST_USER_FOR_CREATE);
        verify(mapper).toDto(TEST_USER_FOR_CREATE);
        verify(mapper).toEntity(TEST_USER_DTO_FOR_CREATE);
    }

    @SneakyThrows
    @Test
    public void checkCreateUser_errorFlow() {
        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(new UserDto())))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    public void checkUpdateUser_successFlow() {
        when(userService.updateUser(TEST_USER)).thenReturn(TEST_USER);
        when(mapper.toDto(TEST_USER)).thenReturn(TEST_USER_DTO);
        when(mapper.toEntity(TEST_USER_DTO)).thenReturn(TEST_USER);


        mockMvc.perform(put(url + "/" + TEST_USER.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(TEST_USER_DTO)))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.id").value(TEST_USER.getId())))
                .andExpect(jsonPath("$.name").value(TEST_USER.getName()));

        verify(userService).updateUser(TEST_USER);
        verify(mapper).toDto(TEST_USER);
        verify(mapper).toEntity(TEST_USER_DTO);
    }

    @SneakyThrows
    @Test
    public void checkUpdateUser_errorFlow() {
        mockMvc.perform(put(url + "/" + FAKE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(new UserDto())))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    public void checkDeleteUser_successFlow() {
        doNothing().when(userService).deleteUserById(TEST_USER.getId());

        mockMvc.perform(delete(url + "/" + TEST_USER.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());

        verify(userService).deleteUserById(TEST_USER.getId());
    }

    @SneakyThrows
    @Test
    public void checkDeleteUser_errorFlow() {
        doThrow(new EntityNotFoundException("User with a specified id isn't found!")).when(userService).deleteUserById(FAKE_ID);

        mockMvc.perform(delete(url + "/" + FAKE_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User with a specified id isn't found!"));

        verify(userService).deleteUserById(FAKE_ID);
    }
}
