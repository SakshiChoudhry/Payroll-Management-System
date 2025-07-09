package com.epam.campusTrainings.employeePayroll.exceptions;

public class JobTitleNotFoundException extends RuntimeException{
    public JobTitleNotFoundException(String message){
        super(message);
    }
}
