package com.renegz.pnccontroller.repositories;

import com.renegz.pnccontroller.domain.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID>{
    Optional<Book> findByIsbn(String isbn);
    void deleteByIsbn(String isbn);
}
