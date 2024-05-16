package com.renegz.pnccontroller.domain.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SaveBookDTO {

    @NotEmpty
    @Pattern(regexp = "\\d{9}-\\d")
    private String ISBN;

    @NotEmpty( message = "El titulo no puede estar vacio")
    private String title;

    @NotEmpty
    @Pattern(regexp = "^CAT[0-9]{3}$")
    private String category;
}
