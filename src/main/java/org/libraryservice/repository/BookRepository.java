package org.libraryservice.repository;

import org.libraryservice.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    Book findBookById(Long idBook);

    Book findBookByIdAndIsFreeIsTrue(Long idBook);

    Book findBookByIdAndIsFreeIsFalse(Long idBook);
}
