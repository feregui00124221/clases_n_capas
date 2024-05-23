package com.renegz.pnccontroller.repositories;

import com.renegz.pnccontroller.domain.dtos.BookLoanDTO;
import com.renegz.pnccontroller.domain.entities.Book;
import com.renegz.pnccontroller.domain.entities.BookLoan;
import com.renegz.pnccontroller.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookLoansRepository extends JpaRepository<BookLoan, UUID> {

    boolean existsByBookAndAndReturnDateIsNull(Book book);

    Optional<BookLoan> findBookLoanByBookAndAndReturnDateIsNull(Book book);

    List<BookLoan> findBookLoanByBook(Book book);

    List<BookLoan> findBookLoanByUser(User user);

    List<BookLoan> findAllByReturnDateIsNull();
}
