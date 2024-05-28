package com.renegz.pnccontroller.domain.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateBookLoanByDurationDTO {
    @NotEmpty
    private String username;
    @NotEmpty
    private String isbn;
    @NotNull
    private Integer loanDuration;
}
