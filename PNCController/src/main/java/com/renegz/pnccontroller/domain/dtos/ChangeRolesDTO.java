package com.renegz.pnccontroller.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class ChangeRolesDTO {
    @NotBlank
    private String identifier;

    @NotEmpty
    private List<@NotBlank String> roles;
}
