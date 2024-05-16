package com.renegz.pnccontroller.utils;

import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ErrorsTool {
    public Map<String, List<String>> mapErrors(List<FieldError> errors){
        Map<String, List<String>> errorsMap = new HashMap<>();
        errors.forEach(
                error -> {
                    List<String> _errors = errorsMap
                            .getOrDefault(error.getField(), new ArrayList<>());
                    _errors.add(error.getDefaultMessage());
                    errorsMap.put(error.getField(), _errors);
                }
        );
        return errorsMap;
    }
}
