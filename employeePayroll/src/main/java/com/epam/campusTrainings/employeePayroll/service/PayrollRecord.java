package com.epam.campusTrainings.employeePayroll.service;

import org.springframework.stereotype.Component;


public class PayrollRecord {
    private int employeeId;
    private String employeeName;
    private String jobTitle;
    private double salary;

    public PayrollRecord(int employeeId, String employeeName, String jobTitle, double salary) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.jobTitle = jobTitle;
        this.salary = salary;
    }

    // Getters
    public int getEmployeeId() { return employeeId; }
    public String getEmployeeName() { return employeeName; }
    public String getJobTitle() { return jobTitle; }
    public double getSalary() { return salary; }
}