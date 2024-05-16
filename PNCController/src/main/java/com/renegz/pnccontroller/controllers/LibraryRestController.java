package com.renegz.pnccontroller.controllers;

import com.renegz.pnccontroller.domain.dtos.GeneralResponse;
import com.renegz.pnccontroller.domain.dtos.SaveBookDTO;
import com.renegz.pnccontroller.domain.entities.Book;
import com.renegz.pnccontroller.domain.entities.Category;
import com.renegz.pnccontroller.services.BookService;
import com.renegz.pnccontroller.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/library")
public class LibraryRestController {

    private final CategoryService categoryService;
    private final BookService bookService;

    public LibraryRestController(BookService bookService, CategoryService categoryService) {
        this.bookService = bookService;
        this.categoryService = categoryService;
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
}
