package org.libraryservice.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.libraryservice.LibraryServiceApplication;
import org.libraryservice.dto.BookDto;
import org.libraryservice.dto.ResponseMessageDto;
import org.libraryservice.dto.UserDto;
import org.libraryservice.mapper.UserDtoMapper;
import org.libraryservice.repository.BookRepository;
import org.libraryservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = LibraryServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest extends AbstractContainer {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestRestTemplate template;
    @Autowired
    private UserDtoMapper userDtoMapper;

    private HttpHeaders headers = new HttpHeaders();
    private HttpEntity<UserDto> request;

    private final String exceptionMessage = "User with a specified id isn't found!";
    private UserDto TEST_USER_DTO_TO_CREATE, TEST_USER_DTO;

    @BeforeEach
    void beforeEach() {
        TEST_USER_DTO_TO_CREATE = UserDto.builder().name("Test user name").build();
        TEST_USER_DTO = UserDto.builder().id(1L).name("Test user name").build();
    }


    @AfterEach
    void afterEach() {
        bookRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void createUserTest_successFlow() {
        final String url = "/api/users";
        UserDto requestDto = TEST_USER_DTO_TO_CREATE;
        request = new HttpEntity<>(requestDto, headers);

        //Make call
        ResponseEntity<UserDto> response =
                this.template.exchange(url, HttpMethod.POST, request, UserDto.class);

        //Verify request succeed
        assertEquals(201, response.getStatusCodeValue());
        assertThat(requestDto).isEqualToIgnoringGivenFields(response.getBody(), "id");
    }

    @Test
    void createUserTestWithMissedRequiredField_unSuccessFlow() {
        final String url = "/api/users";
        //Make field name empty
        UserDto requestDto = TEST_USER_DTO_TO_CREATE;
        requestDto.setName(" ");
        request = new HttpEntity<>(requestDto, headers);

        //Make call
        ResponseEntity<ResponseMessageDto> response =
                this.template.exchange(url, HttpMethod.POST, request, ResponseMessageDto.class);

        //Verify this exception because of validation missed field
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void updateUserTest_successFlow() {
        UserDto requestDto = TEST_USER_DTO;
        Long id = userRepository.save(userDtoMapper.toEntity(requestDto)).getId();

        //Make call
        requestDto.setName("new user name");
        final String url = "/api/users/" + id;
        request = new HttpEntity<>(requestDto, headers);
        ResponseEntity<UserDto> response =
                this.template.exchange(url, HttpMethod.PUT, request, UserDto.class);

        //Verify request succeed
        assertEquals(200, response.getStatusCodeValue());
        assertThat(requestDto).isEqualToIgnoringGivenFields(response.getBody(), "id");
    }

    @Test
    void updateUserTest_unSuccessFlow() {
        UserDto requestDto = TEST_USER_DTO;

        //Make call
        requestDto.setName("new user name");
        final String url = "/api/users/" + Integer.MAX_VALUE;
        request = new HttpEntity<>(requestDto, headers);
        ResponseEntity<ResponseMessageDto> response = this.template.exchange(
                url, HttpMethod.PUT, request, ResponseMessageDto.class);

        //Verify AnimalNotFoundException exception
        assertEquals(404, response.getStatusCodeValue());
        assertEquals(exceptionMessage, response.getBody().getMessage());
    }

    @Test
    void getUserByIdTest_successFlow() {
        UserDto requestDto = TEST_USER_DTO;
        Long userId = userRepository.save(userDtoMapper.toEntity(requestDto)).getId();

        //Make call
        final String url = "/api/users/" + userId;
        ResponseEntity<UserDto> response =
                this.template.exchange(url, HttpMethod.GET, null, UserDto.class);

        //Verify request succeed
        assertEquals(200, response.getStatusCodeValue());
        assertThat(requestDto).isEqualToIgnoringGivenFields(response.getBody(), "id");
    }


    @Test
    void getUserByIdTest_unSuccessFlow() {
        //Make call
        final String url = "/api/users/" + Integer.MAX_VALUE;
        ResponseEntity<ResponseMessageDto> response = this.template.exchange(
                url, HttpMethod.GET, null,
                ResponseMessageDto.class);

        //Verify UserNotFoundException exception
        assertEquals(404, response.getStatusCodeValue());
        assertEquals(exceptionMessage, response.getBody().getMessage());
    }

    @Test
    void deleteUser_successFlow() {
        UserDto requestDto = TEST_USER_DTO;
        Long id = userRepository.save(userDtoMapper.toEntity(requestDto)).getId();

        final String url = "/api/users/" + id;

        ResponseEntity<ResponseEntity> response
                = template.exchange(url, HttpMethod.DELETE, null, ResponseEntity.class);

        //Checking if entity was deleted
        assertEquals(204, response.getStatusCodeValue());

    }
}
