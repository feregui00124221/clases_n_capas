package com.renegz.pnccontroller.domain.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserRegisterDTO {
    @NotBlank
    private String username;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least 8 characters, one uppercase letter, one lowercase letter, one number and one special character"
    )
    private String password;
}
