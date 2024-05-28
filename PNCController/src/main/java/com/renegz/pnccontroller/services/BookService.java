package com.renegz.pnccontroller.services;

import com.renegz.pnccontroller.domain.dtos.SaveBookDTO;
import com.renegz.pnccontroller.domain.entities.Book;
import com.renegz.pnccontroller.domain.entities.Category;
import org.hibernate.query.Page;

import java.util.List;

public interface BookService {
    void save(SaveBookDTO info, Category category);
    List<Book> findAll();

    org.springframework.data.domain.Page<Book> findAllPageable(int page, int size);
    Page findAllByAuthor(String author, int page, int size);
    Page findBooksByCategory(String category, int page, int size);

    Book findByIsbn(String isbn);
    void deleteByIsbn(String isbn);
}
