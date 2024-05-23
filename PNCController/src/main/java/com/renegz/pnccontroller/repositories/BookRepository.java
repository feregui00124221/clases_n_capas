package com.renegz.pnccontroller.repositories;

import com.renegz.pnccontroller.domain.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID>{
    Optional<Book> findByIsbn(String isbn);
    void deleteByIsbn(String isbn);
}
