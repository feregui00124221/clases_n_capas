package com.renegz.pnccontroller.domain.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SaveBookDTO {

    @NotEmpty(message = "Verifique el ISBN ingresado")
    //@Pattern(regexp = "\\d{9}-\\d")
    private String isbn;

    @NotEmpty(message = "Verifique el formato del titulo ingresado")
    private String title;

    @NotEmpty
    //@Pattern(regexp = "^CT_[0-9]{3}$")
    private String category;

    @NotEmpty
    private String author;
}
