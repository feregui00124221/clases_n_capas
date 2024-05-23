package com.renegz.pnccontroller.utils;

import lombok.Value;
import org.springframework.stereotype.Component;

@Component
public class JWTTools {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.exptime}")
    private Integer exp;
}
