package com.renegz.pnccontroller.controllers;

import com.renegz.pnccontroller.domain.dtos.CreateBookLoanDTO;
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
    public ResponseEntity<GeneralResponse> loanBook(@RequestBody @Valid CreateBookLoanDTO info){
        User user = userService.findUserByIdentifier(info.getUsername());

        if(user == null){
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }

        Book book = bookService.findByIsbn(info.getIsbn());

        if(book == null){
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "Libro no encontrado");
        }

        if(bookLoanService.isLoaned(book)){
            return GeneralResponse.getResponse(HttpStatus.CONFLICT, "El libro ya está prestado");
        }

        bookLoanService.createLoan(book, user, info.getLoanDuration());

        return GeneralResponse.getResponse(HttpStatus.OK, "Libro prestado");
    }

    @PostMapping("/return-book/{isbn}")
    public ResponseEntity<GeneralResponse> returnBook(@PathVariable String isbn){
        Book book = bookService.findByIsbn(isbn);

        if(book == null){
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "Libro no encontrado");
        }

        if(!bookLoanService.isLoaned(book)){
            return GeneralResponse.getResponse(HttpStatus.CONFLICT, "El libro no está prestado");
        }

        bookLoanService.returnBook(bookLoanService.findActiveByBook(book));

        return GeneralResponse.getResponse(HttpStatus.OK, "Libro devuelto");
    }


    @GetMapping("/loans")
    public ResponseEntity<GeneralResponse> findAllLoans(){
        return GeneralResponse.getResponse(HttpStatus.OK, "List of loans!", bookLoanService.findAll());
    }

    @GetMapping("/loans-active")
    public ResponseEntity<GeneralResponse> findAllActiveLoans(){
        return GeneralResponse.getResponse(HttpStatus.OK, "List of active loans!", bookLoanService.findAllActive());
    }
}
