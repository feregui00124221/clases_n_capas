package com.renegz.pnccontroller.services.implementations;

import com.renegz.pnccontroller.domain.dtos.SaveBookDTO;
import com.renegz.pnccontroller.domain.entities.Book;
import com.renegz.pnccontroller.domain.entities.Category;
import com.renegz.pnccontroller.repositories.BookRepository;
import com.renegz.pnccontroller.services.BookService;
import jakarta.transaction.TransactionScoped;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service //Ahora ya lo puede ocupas el entityManager para poder inyectarlo
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void save(SaveBookDTO info, Category category) {
        Book book = this.findByIsbn(info.getISBN());

        if (book == null) {
            book = new Book();
        }

        book.setIsbn(info.getISBN());
        book.setTitle(info.getTitle());
        book.setCategory(category);
        bookRepository.save(book);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn).orElse(null);
    }

    @Override
    public void deleteByIsbn(String isbn) {
        bookRepository.deleteByIsbn(isbn);
    }
}
