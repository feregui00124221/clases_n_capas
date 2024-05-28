package com.renegz.pnccontroller.domain.dtos;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.Date;

@Data
public class CreateBookLoanByDateDTO {
    @NotNull
    private String username;

    @NotNull
    private String isbn;

    @NotNull
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Formato de fecha incorrecto. Use yyyy-MM-dd")
    private String returnDate;
}
