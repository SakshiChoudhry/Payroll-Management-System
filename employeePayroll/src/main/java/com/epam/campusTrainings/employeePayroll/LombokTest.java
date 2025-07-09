package com.epam.campusTrainings.employeePayroll;

import lombok.Data;

@Data
public class LombokTest {
    private String message;

    public static void main(String[] args) {
        LombokTest test = new LombokTest();
        test.setMessage("Lombok is working!");
        System.out.println(test.getMessage());
    }
}
