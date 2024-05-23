package com.renegz.pnccontroller.domain.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.renegz.pnccontroller.domain.entities.Book;
import com.renegz.pnccontroller.domain.entities.User;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookLoanDTO {
    @NotEmpty
    private Date loanDate;

    private Date returnDate;

    @NotEmpty
    private Date dueDate;

    @NotEmpty
    private Book book;

    @NotEmpty
    private String username;
}
