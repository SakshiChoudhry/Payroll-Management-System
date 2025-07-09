package com.epam.campusTrainings.employeePayroll.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RegisterRequest {
    private String name;
    private String password;
    private Integer departmentId;
    private Integer jobTitleId;
    private String email;
}
