package com.renegz.pnccontroller.controllers;

import com.renegz.pnccontroller.domain.dtos.GeneralResponse;
import com.renegz.pnccontroller.domain.dtos.SaveBookDTO;
import com.renegz.pnccontroller.domain.entities.Book;
import com.renegz.pnccontroller.domain.entities.Category;
import com.renegz.pnccontroller.services.BookService;
import com.renegz.pnccontroller.services.CategoryService;
import com.renegz.pnccontroller.utils.ErrorsTool;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/library")
public class LibraryRestController {

    private final CategoryService categoryService;
    private final ErrorsTool errorsTool;
    private final BookService bookService;

    public LibraryRestController(ErrorsTool errorsTool, BookService bookService, CategoryService categoryService) {
        this.errorsTool = errorsTool;
        this.bookService = bookService;
        this.categoryService = categoryService;
    }

    @GetMapping("/all")
    public ResponseEntity<GeneralResponse> findAll(){
//        return new ResponseEntity<>(
//                new GeneralResponse("List of books!", bookService.findAll()),
//                HttpStatus.OK
//        );
        return GeneralResponse.getResponse(
                HttpStatus.OK,
                "List of books!",
                bookService.findAll());
    }

    @PostMapping("/save")
    public ResponseEntity<GeneralResponse> saveBook(@RequestBody @Valid SaveBookDTO info) {
//        if (errors.hasErrors()) {
//            return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, errorsTool.mapErrors(errors.getFieldErrors()));
//        }

        Category category = categoryService.findCategoryByCode(info.getCategory());

        if (category == null){
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "Categoría no encontrada");
        }

        //TODO: Save Book
        bookService.save(info, category);

        return GeneralResponse.getResponse(HttpStatus.OK, "Libro guardado");
    }

    @DeleteMapping("/{isbn}")
    public ResponseEntity<GeneralResponse> deleteByIsbn(@PathVariable String isbn){
        Book book = bookService.findByIsbn(isbn);
        if (book == null){
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND);
        }

        bookService.deleteByIsbn(isbn);

        return GeneralResponse.getResponse(HttpStatus.OK);
    }

    @GetMapping("/category")
    public ResponseEntity<GeneralResponse> findAllCategories(){
        return GeneralResponse.getResponse(
                categoryService.findAllCategories()
        );
    }

    @GetMapping("/category/{code}")
    public ResponseEntity<GeneralResponse> findCategoryByCode(@PathVariable String code){

        Category category = categoryService.findCategoryByCode(code);

        if (category == null){
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND);
        }
        return GeneralResponse.getResponse(category);
    }
}
