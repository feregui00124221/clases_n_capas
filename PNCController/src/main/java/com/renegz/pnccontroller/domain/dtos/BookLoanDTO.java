package com.renegz.pnccontroller.domain.dtos;

import com.renegz.pnccontroller.domain.entities.Book;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookLoanDTO {
    @NotEmpty
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Date must be in format yyyy-MM-dd")
    private Date loanDate;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Date must be in format yyyy-MM-dd")
    private Date returnDate;

    @NotEmpty
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Date must be in format yyyy-MM-dd")
    private Date dueDate;

    @NotEmpty
    private Book book;

    @NotEmpty
    private String username;
}
