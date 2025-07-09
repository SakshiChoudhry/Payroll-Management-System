package com.epam.campusTrainings.employeePayroll.controller;

import com.epam.campusTrainings.employeePayroll.dto.DepartmentDTO;
import com.epam.campusTrainings.employeePayroll.service.PayrollService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentController.class);

    @Autowired
    private PayrollService payrollService;

    @GetMapping
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments() {
        logger.info("Fetching all departments");
        List<DepartmentDTO> departmentDTOS = payrollService.getAllDepartments();
        logger.info("Retrieved {} departments", departmentDTOS.size());
        return ResponseEntity.ok(departmentDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDTO> getDepartmentById(@PathVariable int id) {
        logger.info("Fetching department with id: {}", id);
        if (!payrollService.isItAValidDepartmentId(id)) {
            logger.warn("Department with id {} not found", id);
            return ResponseEntity.notFound().build();
        }
        DepartmentDTO departmentDTO = payrollService.getDepartmentById(id);
        logger.info("Retrieved department with id: {}", id);
        return ResponseEntity.ok(departmentDTO);
    }

    @GetMapping("/{departmentName}/average-salary")
    public ResponseEntity<Double> getAverageSalaryByDepartment(@PathVariable String departmentName) {
        logger.info("Calculating average salary for department: {}", departmentName);
        double averageSalary = payrollService.calculateAverageSalaryByDepartment(departmentName);
        logger.info("Average salary for department {}: {}", departmentName, averageSalary);
        return ResponseEntity.ok(averageSalary);
    }

    @PostMapping
    public ResponseEntity<?> createDepartment(@RequestBody DepartmentDTO departmentDTO) {
        logger.info("Creating department with name: {}", departmentDTO.getName());
        try {
            payrollService.createDepartment(departmentDTO.getName());
            logger.info("Department created successfully with name: {}", departmentDTO.getName());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            logger.error("Failed to create department: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
