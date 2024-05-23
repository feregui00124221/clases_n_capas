package com.renegz.pnccontroller.services.implementations;

import com.renegz.pnccontroller.domain.dtos.BookLoanDTO;
import com.renegz.pnccontroller.domain.entities.Book;
import com.renegz.pnccontroller.domain.entities.BookLoan;
import com.renegz.pnccontroller.domain.entities.User;
import com.renegz.pnccontroller.repositories.BookLoansRepository;
import com.renegz.pnccontroller.services.BookLoanService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookLoanServiceImpl implements BookLoanService {

    final
    BookLoansRepository bookLoansRepository;

    public BookLoanServiceImpl(BookLoansRepository bookLoansRepository) {
        this.bookLoansRepository = bookLoansRepository;
    }

    @Override
    public void createLoan(Book book, User user, int loanDuration) {
        BookLoan bookLoan = new BookLoan();

        bookLoan.setBook(book);
        bookLoan.setUser(user);

        bookLoan.setLoanDate(Date.from(Instant.now()));

        bookLoan.setDueDate(
                Date.from(Instant.now().plusSeconds((long) loanDuration * 24 * 60 * 60))
        );

        bookLoansRepository.save(bookLoan);
    }

    @Override
    public BookLoan findById(UUID id) {
        return bookLoansRepository.findById(id).orElse(null);
    }

    @Override
    public BookLoan findActiveByBook(Book book) {
        return bookLoansRepository.findBookLoanByBookAndAndReturnDateIsNull(book).orElse(null);
    }

    @Override
    public List<BookLoan> findAll() {
        return bookLoansRepository.findAll();
    }

    @Override
    public List<BookLoan> findAllByBook(Book book) {
        return bookLoansRepository.findBookLoanByBook(book);
    }

    @Override
    public List<BookLoan> findAllByUser(User user) {
        return bookLoansRepository.findBookLoanByUser(user);
    }

    @Override
    public List<BookLoanDTO> findAllActive() {
        return bookLoansRepository
                .findAllByReturnDateIsNull()
                .stream()
                .map(bookLoan -> new BookLoanDTO(
                        bookLoan.getLoanDate(),
                        bookLoan.getReturnDate(),
                        bookLoan.getDueDate(),
                        bookLoan.getBook(),
                        bookLoan.getUser().getUsername()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isLoaned(Book book) {
        return bookLoansRepository.existsByBookAndAndReturnDateIsNull(book);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void returnBook(BookLoan bookLoan) {
        bookLoan.setReturnDate(Date.from(Instant.now()));
        bookLoansRepository.save(bookLoan);
    }
}
