package com.renegz.pnccontroller.domain.dtos;

import lombok.Data;

import java.util.List;

@Data
public class WhoamiDTO {
    private String username;
    private String email;
    private List<String> roles;
}
