package org.libraryservice.controller;

import org.libraryservice.mapper.UserDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.libraryservice.service.UserService;

@RestController
@RequestMapping("api/users")
public class UserController {

    UserService userService;
    UserDtoMapper mapper;

    @Autowired
    public UserController(UserService userService, UserDtoMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }
}
