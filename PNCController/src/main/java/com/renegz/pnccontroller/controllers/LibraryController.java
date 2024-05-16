package com.renegz.pnccontroller.controllers;

import com.renegz.pnccontroller.domain.dtos.SaveBookDTO;
import com.renegz.pnccontroller.domain.entities.Book;
import com.renegz.pnccontroller.services.BookService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/library")
@Slf4j
public class LibraryController {
    @Autowired
    private BookService bookService;

    @GetMapping("/all")
    public String getAllBooks(Model model){
        model.addAttribute("books", bookService.findAll());
        return "book-list";
    }

    @GetMapping("/rest")
    public String mainRest(Model model){
        model.addAttribute("books", bookService.findAll());
        return "index-rest";
    }

    @GetMapping("/")
    public String getSaveForm(){
        return "save-book";
    }

//    @PostMapping("/save")
//    public String saveBook(@ModelAttribute @Valid SaveBookDTO info, BindingResult errors){
//
//        if (errors.hasErrors()){
//            log.error("ha habido un error al enviar el formulario: {}", errors.getAllErrors());
//            return "errorsito";
//        }
//
//        Category category = categoryService.findCategoryByCode(info.getCategory());
//        bookService.save(info);
//
//        return "redirect:/library/all";
//    }
}
