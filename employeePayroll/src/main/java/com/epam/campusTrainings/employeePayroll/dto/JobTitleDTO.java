package com.epam.campusTrainings.employeePayroll.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobTitleDTO {
    private int id;

    @NotBlank(message = "Job Title cannot be empty")
    private String title;

    @NotNull(message = "Job Title ID is required")
    @Min(value = 1, message = "Job Title ID must be a positive number")
    private double baseSalary;

}
