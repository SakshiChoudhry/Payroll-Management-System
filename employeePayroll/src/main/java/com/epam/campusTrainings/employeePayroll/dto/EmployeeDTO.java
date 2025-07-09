package com.epam.campusTrainings.employeePayroll.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    private int employeeId;

    @Pattern(regexp = "^[a-zA-Z]+$", message = "You can only enter alphabets in a string stupid!!")
    @NotBlank(message = "Employee name cannot be empty")
    private String name;

    @NotNull(message = "Department ID is required")
    @Min(value = 1, message = "Department ID must be a positive number")
    private int departmentId;

    @NotNull(message = "Job Title ID is required")
    @Min(value = 1, message = "Job Title ID must be a positive number")
    private int jobTitleId;

    @NotNull
    private String email;
}
