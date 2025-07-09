package com.epam.campusTrainings.employeePayroll.controller;

import com.epam.campusTrainings.employeePayroll.dto.JobTitleDTO;
import com.epam.campusTrainings.employeePayroll.service.PayrollService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobTitles")
public class JobTitleController {

    private static final Logger logger = LoggerFactory.getLogger(JobTitleController.class);

    @Autowired
    private PayrollService payrollService;

    @GetMapping
    public ResponseEntity<List<JobTitleDTO>> getAllJobTitles() {
        logger.info("Fetching all job titles");
        List<JobTitleDTO> jobTitleDTOs = payrollService.getAllJobTitles();
        logger.info("Retrieved {} job titles", jobTitleDTOs.size());
        return ResponseEntity.ok(jobTitleDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobTitleDTO> getJobTitleById(@PathVariable int id) {
        logger.info("Fetching job title with id: {}", id);
        JobTitleDTO jobTitleDTO = payrollService.getJobTitleById(id);
        if (jobTitleDTO == null) {
            logger.warn("Job title with id {} not found", id);
            return ResponseEntity.notFound().build();
        }
        logger.info("Retrieved job title with id: {}", id);
        return ResponseEntity.ok(jobTitleDTO);
    }

    @PostMapping
    public ResponseEntity<?> createJobTitle(@RequestBody JobTitleDTO jobTitleDTO) {
        logger.info("Creating job title with title: {}", jobTitleDTO.getTitle());
        try {
            payrollService.createJobTitle(jobTitleDTO.getTitle(), jobTitleDTO.getBaseSalary());
            logger.info("Job title '{}' created successfully", jobTitleDTO.getTitle());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            logger.error("Failed to create job title: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
