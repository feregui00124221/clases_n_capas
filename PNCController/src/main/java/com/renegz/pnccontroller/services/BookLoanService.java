package com.renegz.pnccontroller.services;

import com.renegz.pnccontroller.domain.dtos.BookLoanDTO;
import com.renegz.pnccontroller.domain.dtos.DateCheckerDTO;
import com.renegz.pnccontroller.domain.entities.Book;
import com.renegz.pnccontroller.domain.entities.BookLoan;
import com.renegz.pnccontroller.domain.entities.User;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface BookLoanService {

    void createLoanByDuration(Book book, User user, int loanDuration);
    void createLoanByDate(Book book, User user, Date returnDate);

    DateCheckerDTO checkLoanDateFormat(String returnDate);
    Date getReturnDate(String returnDate);

    BookLoan findById(UUID id);
    BookLoan findActiveByBook(Book book);

    List<BookLoan> findAll();
    List<BookLoan> findAllByBook(Book book);
    List<BookLoan> findAllByUser(User user);
    List<BookLoanDTO> findAllActive();

    boolean isLoaned(Book book);

    void returnBook(BookLoan bookLoan);
}
