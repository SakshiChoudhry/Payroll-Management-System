package com.epam.campusTrainings.employeePayroll.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayrollRecordDTO {
    private int employeeId;
    private String employeeName;
    private String jobTitle;
    private double salary;

    // No need for explicit constructors or getters/setters as Lombok generates them
    public PayrollRecordDTO() {}

    public PayrollRecordDTO(int employeeId, String employeeName, String jobTitle, double salary) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.jobTitle = jobTitle;
        this.salary = salary;
    }
}
