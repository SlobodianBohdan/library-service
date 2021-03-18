package org.libraryservice.service;

import org.libraryservice.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAll();

    User createdUser (User user);

    User updateUser (User user);

    void deleteUserById(Long idUser);

    void addBookByIdForUserById(Long idBook, Long idUser);
}
