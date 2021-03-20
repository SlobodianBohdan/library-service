package org.libraryservice.service.impl;

import org.libraryservice.entity.User;
import org.libraryservice.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.libraryservice.repository.BookRepository;
import org.libraryservice.repository.UserRepository;
import org.libraryservice.service.UserService;

import java.util.List;

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
        findByIdOrThrowException(userRepository, idUser);
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
        findByIdOrThrowException(userRepository, idUser);
        userRepository.deleteById(idUser);
    }

    private <T> T findByIdOrThrowException(JpaRepository<T, Long> repository, Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity is not found!"));
    }

}
