package com.renegz.pnccontroller.services.implementations;

import com.renegz.pnccontroller.domain.dtos.BookLoanDTO;
import com.renegz.pnccontroller.domain.dtos.DateCheckerDTO;
import com.renegz.pnccontroller.domain.entities.Book;
import com.renegz.pnccontroller.domain.entities.BookLoan;
import com.renegz.pnccontroller.domain.entities.User;
import com.renegz.pnccontroller.repositories.BookLoansRepository;
import com.renegz.pnccontroller.services.BookLoanService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookLoanServiceImpl implements BookLoanService {

    final
    BookLoansRepository bookLoansRepository;

    final
    UserServiceImpl userService;

    final
    BookServiceImpl bookService;

    public BookLoanServiceImpl(BookLoansRepository bookLoansRepository, UserServiceImpl userService, BookServiceImpl bookService) {
        this.bookLoansRepository = bookLoansRepository;
        this.userService = userService;
        this.bookService = bookService;
    }

    @Override
    public void createLoanByDuration(Book book, User user, int loanDuration) {
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
    public void createLoanByDueDate(Book book, User user, Date returnDate) {
        BookLoan bookLoan = new BookLoan();

        bookLoan.setBook(book);
        bookLoan.setUser(user);

        bookLoan.setLoanDate(Date.from(Instant.now()));
        bookLoan.setDueDate(returnDate);

        bookLoansRepository.save(bookLoan);
    }

    @Override
    public void createLoanByIssueDate(Book book, User user, Date issueDate) {
        BookLoan bookLoan = new BookLoan();

        bookLoan.setBook(book);
        bookLoan.setUser(user);

        bookLoan.setLoanDate(issueDate);

        Date dueDate = Date.from(issueDate.toInstant().plusSeconds(7 * 24 * 60 * 60));
        bookLoan.setDueDate(dueDate);

        bookLoansRepository.save(bookLoan);
    }

    @Override
    public DateCheckerDTO checkDateFormat(String returnDate) {
        String[] dateParts = returnDate.split("-");

        if(dateParts.length != 3 || (dateParts[0].length() != 4 || dateParts[1].length() != 2 || dateParts[2].length() != 2)){
            return new DateCheckerDTO(false, "Formato de fecha incorrecto, debe ser yyyy-mm-dd");
        }

        if(!dateParts[1].matches("0[1-9]|1[0-2]")){
            return new DateCheckerDTO(false, "Fecha inválida, mes incorrecto");
        }

        if(!dateParts[2].matches("0[1-9]|[12][0-9]|3[01]")){
            return new DateCheckerDTO(false, "Fecha inválida, día incorrecto");
        }

        return new DateCheckerDTO(true, "Fecha válida");
    }

    @Override
    public Date StringToDate(String returnDate) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(returnDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
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
    public boolean willBeLoaned(Book book, Date issueDate) {
        return true;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void returnBook(BookLoan bookLoan) {
        bookLoan.setReturnDate(Date.from(Instant.now()));
        bookLoansRepository.save(bookLoan);
    }
}
