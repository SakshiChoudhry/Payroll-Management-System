package com.epam.campusTrainings.employeePayroll.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {
    // Basic status information
    private boolean success;
    private String message;

    // User information
    private String username;


    private String email;

    // Timestamp of registration
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();


    public static RegisterResponse success(String username,String email) {
        return RegisterResponse.builder()
                .success(true)
                .message("User registered successfully")
                .username(username)
                .email(email)
                .build();
    }

    public static RegisterResponse failure(String message) {
        return RegisterResponse.builder()
                .success(false)
                .message(message)
                .build();
    }
}
