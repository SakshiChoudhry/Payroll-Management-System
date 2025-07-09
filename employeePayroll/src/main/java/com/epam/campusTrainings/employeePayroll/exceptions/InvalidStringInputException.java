package com.epam.campusTrainings.employeePayroll.exceptions;

public class InvalidStringInputException extends RuntimeException {
    public InvalidStringInputException(String message) {
        super(message);
    }
}