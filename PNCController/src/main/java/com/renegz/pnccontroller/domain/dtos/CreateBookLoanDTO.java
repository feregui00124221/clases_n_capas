package com.renegz.pnccontroller.domain.dtos;

import com.renegz.pnccontroller.domain.entities.Book;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateBookLoanDTO {
    @NotEmpty
    private String username;
    @NotEmpty
    private String isbn;
    @NotNull
    private Integer loanDuration;
}
