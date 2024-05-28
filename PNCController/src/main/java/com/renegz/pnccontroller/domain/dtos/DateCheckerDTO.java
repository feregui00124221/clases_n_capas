package com.renegz.pnccontroller.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DateCheckerDTO {
    private boolean valid;
    private String message;
}
