package com.renegz.pnccontroller.repositories;

import com.renegz.pnccontroller.domain.entities.Book;
import com.renegz.pnccontroller.domain.entities.BookLoan;
import com.renegz.pnccontroller.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookLoansRepository extends JpaRepository<BookLoan, UUID> {

    boolean existsByBookAndAndReturnDateIsNull(Book book);

    Optional<BookLoan> findBookLoanByBookAndAndReturnDateIsNull(Book book);

    List<BookLoan> findBookLoanByBook(Book book);

    List<BookLoan> findBookLoanByUser(User user);

    List<BookLoan> findAllByReturnDateIsNull();

    // I want a query that, given a desired loanDate and a book, to check if said book will be loaned in that period of time. This is known through the returnDate attribute of the BookLoan entity. If returnDate is null, book has not been returned. returnDate is guaranteed to be no more than 30 days since loanDate.
    boolean existsByBookAndLoanDateBetweenAndReturnDateIsNull(Book book, Date loanDate, Date returnDate);
}
