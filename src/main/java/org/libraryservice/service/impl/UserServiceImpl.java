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

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAll() {
        List<User> listUsers = userRepository.findAll();
        if (listUsers.isEmpty()){
            throw new EntityNotFoundException("Users isn't found!");
        }
        return listUsers;
    }

    @Override
    public User getById(Long idUser) {
        return findByIdOrThrowException(userRepository, idUser);
    }

    @Override
    public User creatUser(User user) {
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
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with a specified id isn't found!"));
    }

}
