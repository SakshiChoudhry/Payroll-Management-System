package com.epam.campusTrainings.employeePayroll.utils;

import com.epam.campusTrainings.employeePayroll.exceptions.InvalidStringInputException;
import org.springframework.stereotype.Component;

public class ValidationUtils {

    public static void validateName(String name, String fieldType) {
        String fieldName = fieldType != null ? fieldType : "Name";

        if (name == null || name.trim().isEmpty()) {
            throw new InvalidStringInputException(fieldName + " cannot be empty");
        }
        if (!name.matches("^[a-zA-Z\\s-']+$")) {
            throw new InvalidStringInputException(fieldName + " contains invalid characters");
        }
    }
    public static void validateId(Integer id, String field) {
        if (id == null) {
            throw new InvalidStringInputException(field+" cannot be null");
        }
        if (id <= 0) {
            throw new InvalidStringInputException(field+" must be a positive number");
        }
    }


}