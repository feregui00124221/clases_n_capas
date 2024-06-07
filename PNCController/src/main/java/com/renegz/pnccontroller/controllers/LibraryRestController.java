package com.renegz.pnccontroller.controllers;

import com.renegz.pnccontroller.domain.dtos.CreateBookLoanByDateDTO;
import com.renegz.pnccontroller.domain.dtos.CreateBookLoanByDurationDTO;
import com.renegz.pnccontroller.domain.dtos.GeneralResponse;
import com.renegz.pnccontroller.domain.dtos.SaveBookDTO;
import com.renegz.pnccontroller.domain.entities.Book;
import com.renegz.pnccontroller.domain.entities.Category;
import com.renegz.pnccontroller.domain.entities.User;
import com.renegz.pnccontroller.services.BookLoanService;
import com.renegz.pnccontroller.services.BookService;
import com.renegz.pnccontroller.services.CategoryService;
import com.renegz.pnccontroller.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/library")
public class LibraryRestController {

    final
    UserService userService;
    private final CategoryService categoryService;
    private final BookService bookService;
    private final BookLoanService bookLoanService;

    public LibraryRestController(BookService bookService, CategoryService categoryService, BookLoanService bookLoanService, UserService userService) {
        this.bookService = bookService;
        this.categoryService = categoryService;
        this.bookLoanService = bookLoanService;
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<GeneralResponse> findAll() {
        return GeneralResponse.getResponse(HttpStatus.OK, "List of books!", bookService.findAll());
    }

    @PostMapping("/save")
    public ResponseEntity<GeneralResponse> saveBook(@RequestBody @Valid SaveBookDTO info) {

        Category category = categoryService.findCategoryByCode(info.getCategory());

        if (category == null) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "Categoría no encontrada");
        }

        bookService.save(info, category);

        return GeneralResponse.getResponse(HttpStatus.OK, "Libro guardado");
    }

    @DeleteMapping("/{isbn}")
    public ResponseEntity<GeneralResponse> deleteByIsbn(@PathVariable String isbn) {
        Book book = bookService.findByIsbn(isbn);
        if (book == null) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "Libro no encontrado");
        }

        bookService.deleteByIsbn(isbn);

        return GeneralResponse.getResponse(HttpStatus.OK, "Libro eliminado");
    }

    @GetMapping("/category")
    public ResponseEntity<GeneralResponse> findAllCategories() {
        return GeneralResponse.getResponse(HttpStatus.OK, "Categorias encontradas", categoryService.findAllCategories());
    }

    @GetMapping("/category/{code}")
    public ResponseEntity<GeneralResponse> findCategoryByCode(@PathVariable String code) {

        Category category = categoryService.findCategoryByCode(code);

        if (category == null) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "Categoría no encontrada");
        }
        return GeneralResponse.getResponse(HttpStatus.OK, category);
    }

    @GetMapping("/allpageable")
    public ResponseEntity<GeneralResponse> findAllPageable(@RequestParam int page, @RequestParam int size) {
        return GeneralResponse.getResponse(HttpStatus.OK, "List of books!", bookService.findAllPageable(page, size));
    }

    @PostMapping("/loan-book")
    public ResponseEntity<GeneralResponse> loanBookByDuration(@RequestBody @Valid CreateBookLoanByDurationDTO info) {

        if (info.getLoanDuration() <= 0) {
            return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "La duración del préstamo debe ser mayor a 0");
        }

        if (info.getLoanDuration() > 30) {
            return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "La duración del préstamo no puede ser mayor a 30 días");
        }

        User user = userService.findUserByIdentifier(info.getUsername());

        if (user == null) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }

        Book book = bookService.findByIsbn(info.getIsbn());

        if (book == null) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "Libro no encontrado");
        }

        if (bookLoanService.isLoaned(book)) {
            return GeneralResponse.getResponse(HttpStatus.CONFLICT, "El libro ya está prestado");
        }

        bookLoanService.createLoanByDuration(book, user, info.getLoanDuration());

        return GeneralResponse.getResponse(HttpStatus.OK, "Libro prestado");
    }

    @PostMapping("/loan-book-by-due-date")
    public ResponseEntity<GeneralResponse> loanBookByDueDate(@RequestBody @Valid CreateBookLoanByDateDTO info) {

        if (!bookLoanService.checkDateFormat(info.getDate()).isValid()) {
            return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, bookLoanService.checkDateFormat(info.getDate()).getMessage());
        }

        Date dueDate = bookLoanService.StringToDate(info.getDate());

        if (new Date().after(dueDate)) {
            return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "La fecha de retorno no puede ser anterior a la fecha actual");
        }

        if(dueDate.after(new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000))){
            return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "La fecha de retorno no puede ser mayor a 30 días a partir de este momento");
        }

        User user = userService.findUserByIdentifier(info.getUsername());

        if (user == null) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }

        Book book = bookService.findByIsbn(info.getIsbn());

        if (book == null) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "Libro no encontrado");
        }

        if (bookLoanService.isLoaned(book)) {
            return GeneralResponse.getResponse(HttpStatus.CONFLICT, "El libro ya está prestado");
        }

        bookLoanService.createLoanByDueDate(book, user, dueDate);

        return GeneralResponse.getResponse(HttpStatus.OK, "Libro prestado");
    }
    
    @PostMapping("/loan-book-by-issue-date")
    public ResponseEntity<GeneralResponse> loanBookByIssueDate(@RequestBody @Valid CreateBookLoanByDateDTO info) {

        if (!bookLoanService.checkDateFormat(info.getDate()).isValid()) {
            return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, bookLoanService.checkDateFormat(info.getDate()).getMessage());
        }

        Date issueDate = bookLoanService.StringToDate(info.getDate());

        // Check if, at the moment the user is requesting the loan, book will not be on loan
        if (bookLoanService.willBeLoaned(bookService.findByIsbn(info.getIsbn()), issueDate)) {
            return GeneralResponse.getResponse(HttpStatus.CONFLICT, "El libro estará prestado en la fecha solicitada");
        }

//        if (new Date().after(issueDate)) {
//            return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "La fecha de retorno no puede ser anterior a la fecha actual");
//        }

        User user = userService.findUserByIdentifier(info.getUsername());

        if (user == null) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }

        Book book = bookService.findByIsbn(info.getIsbn());

        if (book == null) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "Libro no encontrado");
        }

        if (bookLoanService.isLoaned(book)) {
            return GeneralResponse.getResponse(HttpStatus.CONFLICT, "El libro ya está prestado");
        }

        bookLoanService.createLoanByIssueDate(book, user, issueDate);

        return GeneralResponse.getResponse(HttpStatus.OK, "Libro prestado");
    }

    @PostMapping("/return-book/{isbn}")
    public ResponseEntity<GeneralResponse> returnBook(@PathVariable String isbn) {
        Book book = bookService.findByIsbn(isbn);

        if (book == null) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "Libro no encontrado");
        }

        if (!bookLoanService.isLoaned(book)) {
            return GeneralResponse.getResponse(HttpStatus.CONFLICT, "El libro no está prestado");
        }

        bookLoanService.returnBook(bookLoanService.findActiveByBook(book));

        return GeneralResponse.getResponse(HttpStatus.OK, "Libro devuelto");
    }


    @GetMapping("/loans")
    public ResponseEntity<GeneralResponse> findAllLoans() {
        return GeneralResponse.getResponse(HttpStatus.OK, "List of loans!", bookLoanService.findAll());
    }

    @GetMapping("/loans-active")
    public ResponseEntity<GeneralResponse> findAllActiveLoans() {
        return GeneralResponse.getResponse(HttpStatus.OK, "List of active loans!", bookLoanService.findAllActive());
    }
}
