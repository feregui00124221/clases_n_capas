package com.renegz.pnccontroller.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginDTO {
    @NotBlank
    private String identifier;
    @NotBlank
    private String password;
}
