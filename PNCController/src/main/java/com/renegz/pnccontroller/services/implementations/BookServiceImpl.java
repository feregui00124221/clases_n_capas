package com.renegz.pnccontroller.services.implementations;

import com.renegz.pnccontroller.domain.dtos.SaveBookDTO;
import com.renegz.pnccontroller.domain.entities.Book;
import com.renegz.pnccontroller.domain.entities.Category;
import com.renegz.pnccontroller.repositories.BookRepository;
import com.renegz.pnccontroller.services.BookService;
import jakarta.transaction.Transactional;
import org.hibernate.query.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service //Ahora ya lo puede ocupar el entityManager para poder inyectarlo
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void save(SaveBookDTO info, Category category) {
        Book book = this.findByIsbn(info.getIsbn());

        if (book == null) {
            book = new Book();
        }

        book.setIsbn(info.getIsbn());
        book.setTitle(info.getTitle());
        book.setCategory(category);
        book.setAuthor(info.getAuthor());
        bookRepository.save(book);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public org.springframework.data.domain.Page<Book> findAllPageable(int page, int size) {
        return bookRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public Page findAllByAuthor(String author, int page, int size) {
        return null;
    }

    @Override
    public Page findBooksByCategory(String category, int page, int size) {
        return null;
    }

    @Override
    public Book findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn).orElse(null);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteByIsbn(String isbn) {
        Book book = this.findByIsbn(isbn);

        if (book != null) {
            bookRepository.deleteByIsbn(isbn);
        }
    }


}
