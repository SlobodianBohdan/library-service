package org.libraryservice.service.impl;

import org.libraryservice.entity.Book;
import org.libraryservice.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.libraryservice.repository.BookRepository;
import org.libraryservice.repository.UserRepository;
import org.libraryservice.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

// implement main logic and logs here

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private BookRepository bookRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getById(Long idUser) {
        return userRepository.findAllById(idUser);
    }

    @Override
    public User createdUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUserById(Long idUser) {
        userRepository.deleteById(idUser);
    }

}
