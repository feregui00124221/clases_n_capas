package com.renegz.pnccontroller.domain.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateBookLoanByDateDTO {
    @NotNull
    private String username;

    @NotNull
    private String isbn;

    @NotNull
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Formato de fecha incorrecto. Use yyyy-MM-dd")
    private String date;
}
