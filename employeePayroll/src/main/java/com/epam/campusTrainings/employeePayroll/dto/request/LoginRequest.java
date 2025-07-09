package com.epam.campusTrainings.employeePayroll.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LoginRequest {
    private String email;
    private String password;
    private String name;
}
