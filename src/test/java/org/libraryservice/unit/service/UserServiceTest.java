package org.libraryservice.unit.service;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.libraryservice.entity.User;
import org.libraryservice.repository.UserRepository;
import org.libraryservice.service.UserService;
import org.libraryservice.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@WebMvcTest(value = UserService.class)
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;

    private static User user1;
    private static User user2;
    private static User user3;


    @BeforeEach
    public void beforeEach() {

        //Create test users
        user1 = User.builder().name("Den").build();
        user2 = User.builder().name("Mari").build();
        user3 = User.builder().name("Ivan").build();

    }

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void checkAllUsers_successFlow() {
        List<User> expected = Arrays.asList(user1, user2, user3);

        when(userRepository.findAll()).thenReturn(expected);
        List<User> actual = userService.getAll();

        assertEquals(expected, actual);
        verify(userRepository).findAll();
    }

    @Test
    public void checkUserById_successFlow() {
        User expected = User.builder().id(1L).name("Ihor").build();

        when(userRepository.findById(expected.getId())).thenReturn(java.util.Optional.of(expected));
        User actual = userService.getById(expected.getId());

        assertEquals(expected, actual);
        verify(userRepository).findById(expected.getId());
    }

    @Test
    public void checkCreateUser_successFlow() {
        User expected = user1;

        when(userRepository.save(user1)).thenReturn(expected);
        User actual = userService.creatUser(user1);

        assertEquals(expected, actual);
        verify(userRepository).save(user1);
    }

    @Test
    public void checkUpdateUser_successFlow() {
        User expected = user1;

        when(userRepository.save(user1)).thenReturn(expected);
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        User actual = userService.updateUser(user1);

        assertEquals(expected, actual);
        verify(userRepository).save(user1);
        verify(userRepository).findById(user1.getId());
    }

    @Test
    public void checkDeleteUserById_successFlow() {
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        doNothing().when(userRepository).deleteById(user1.getId());

        userService.deleteUserById(user1.getId());

        verify(userRepository).deleteById(user1.getId());
        verify(userRepository).findById(user1.getId());
    }
}
