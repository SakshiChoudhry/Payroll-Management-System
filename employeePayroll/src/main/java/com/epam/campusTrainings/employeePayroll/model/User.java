package com.epam.campusTrainings.employeePayroll.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column
    private String role;


    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    // UserDetails implementation methods

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Base role for all users
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        // Add role based on job title
        if (employee != null && employee.getJobTitle() != null) {
            String jobTitle = employee.getJobTitle().getTitle();

            if ("Manager".equalsIgnoreCase(jobTitle) || "Director".equalsIgnoreCase(jobTitle)) {
                authorities.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
            }

            if ("HR Specialist".equalsIgnoreCase(jobTitle)) {
                authorities.add(new SimpleGrantedAuthority("ROLE_HR"));
            }

            // Could add department-based roles too
            if (employee.getDepartment() != null && "Research and Development".equalsIgnoreCase(employee.getDepartment().getName())) {
                authorities.add(new SimpleGrantedAuthority("ROLE_IT"));
            }
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Regular getters and setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }


    public String getRole() {
        return role;
    }

    public void setEmail(String email) {
        this.email=email;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public void setRole(String role) {
        this.role = role;
    }
}