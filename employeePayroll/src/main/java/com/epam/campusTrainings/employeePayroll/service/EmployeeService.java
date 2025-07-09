package com.epam.campusTrainings.employeePayroll.service;

import com.epam.campusTrainings.employeePayroll.dto.request.LoginRequest;
import com.epam.campusTrainings.employeePayroll.dto.request.RegisterRequest;
import com.epam.campusTrainings.employeePayroll.mappers.EmployeeMapper;
import com.epam.campusTrainings.employeePayroll.model.Department;
import com.epam.campusTrainings.employeePayroll.model.Employee;
import com.epam.campusTrainings.employeePayroll.model.JobTitle;
import com.epam.campusTrainings.employeePayroll.model.User;
import com.epam.campusTrainings.employeePayroll.repository.DepartmentRepository;
import com.epam.campusTrainings.employeePayroll.repository.EmployeeRepository;
import com.epam.campusTrainings.employeePayroll.repository.JobTitleRepository;
import com.epam.campusTrainings.employeePayroll.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class EmployeeService {
    private final PasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;
    private final JobTitleRepository jobTitleRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);


    @Autowired
    public EmployeeService(ObjectMapper objectMapper, PasswordEncoder passwordEncoder, EmployeeRepository employeeRepository, JobTitleRepository jobTitleRepository, DepartmentRepository departmentRepository, UserRepository userRepository, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.passwordEncoder = passwordEncoder;
        this.employeeRepository = employeeRepository;
        this.jobTitleRepository = jobTitleRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public void register(RegisterRequest request) {
        logger.info("Processing registration request for username: {}", request.getName());

        // Validate username is not already taken
        if (userRepository.existsByUsername(request.getName())) {
            logger.error("Username already exists: {}", request.getName());
            throw new IllegalArgumentException("Username already exists");
        }

        try {
            // Get department
            Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new IllegalArgumentException("Department not found with ID: " + request.getDepartmentId()));

            // Get job title
            JobTitle jobTitle = jobTitleRepository.findById(request.getJobTitleId())
                    .orElseThrow(() -> new IllegalArgumentException("Job Title not found with ID: " + request.getJobTitleId()));

            // Create employee
            Employee employee = new Employee(request.getName(), department, jobTitle, request.getEmail());
            employee = employeeRepository.save(employee);
            logger.info("Created employee with ID: {}", employee.getEmployeeId());

            // Create user account
            User user = new User();
            user.setUsername(request.getName());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setEmail(request.getEmail());
            user.setEmployee(employee);

            String role=determineHighestRole(user.getAuthorities());
            logger.info("Role Assigned : {}",role);
            user.setRole(role);

            userRepository.save(user);
            logger.info("Created user account for employee ID: {}", employee.getEmployeeId());

        } catch (Exception e) {
            logger.error("Registration failed: {}", e.getMessage(), e);
            throw new RuntimeException("Registration failed: " + e.getMessage(), e);
        }
    }

    public String login(LoginRequest request) {
        logger.info("Processing login request for username: {}", request.getName());

        try {
            // Use the configured AuthenticationManager to authenticate
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getName(),
                            request.getPassword()
                    )
            );

            logger.info("Login successful for username: {}", request.getName());
            return this.tokenService.generateTokens(authentication);
        } catch (AuthenticationException e) {
            logger.error("Login failed for username {}: {}", request.getName(), e.getMessage());
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    private String determineHighestRole(Collection<? extends GrantedAuthority> authorities) {
        // Check for roles in order of precedence
        if (hasAuthority(authorities, "ROLE_MANAGER")) {
            return "MANAGER";
        } else if (hasAuthority(authorities, "ROLE_HR")) {
            return "HR";
        } else if (hasAuthority(authorities, "ROLE_IT")) {
            return "IT";
        } else {
            return "USER";
        }
    }

    /**
     * Checks if the authorities collection contains a specific authority
     */
    private boolean hasAuthority(Collection<? extends GrantedAuthority> authorities, String authority) {
        for (GrantedAuthority auth : authorities) {
            if (auth.getAuthority().equals(authority)) {
                return true;
            }
        }
        return false;
    }


}
