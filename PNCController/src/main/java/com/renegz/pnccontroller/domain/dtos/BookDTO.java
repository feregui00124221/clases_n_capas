package com.renegz.pnccontroller.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookDTO {
    @NotBlank
    private String isbn;

    @NotBlank
    private String title;

    @NotNull
    private String category;

    @NotEmpty
    private String author;
}
