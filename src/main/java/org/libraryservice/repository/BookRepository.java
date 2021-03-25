package org.libraryservice.repository;

import org.libraryservice.entity.Book;
import org.libraryservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    Book findBookById(Long idBook);

    Book findBookByIdAndIsFreeIsTrue(Long idBook);

    Book findBookByIdAndIsFreeIsFalseAndUser(Long idBook, User user);
}
