package com.epam.campusTrainings.employeePayroll.controller;

import com.epam.campusTrainings.employeePayroll.dto.PayrollRecordDTO;
import com.epam.campusTrainings.employeePayroll.service.PayrollRecord;
import com.epam.campusTrainings.employeePayroll.service.PayrollService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employees/payroll")
public class PayrollController {

    private static final Logger logger = LoggerFactory.getLogger(PayrollController.class);

    private final PayrollService payrollService;

    @Autowired
    public PayrollController(PayrollService payrollService) {
        this.payrollService = payrollService;
    }

    @PostMapping
    public ResponseEntity<Map<String, List<PayrollRecordDTO>>> generateDepartmentWisePayroll() {
        logger.info("Generating department-wise payroll data");
        try {
            Map<String, List<PayrollRecord>> payrollData = payrollService.generateDepartmentWisePayroll();

            Map<String, List<PayrollRecordDTO>> payrollDTOs = new HashMap<>();

            payrollData.forEach((deptName, records) -> {
                List<PayrollRecordDTO> recordDTOs = records.stream()
                        .map(record -> new PayrollRecordDTO(
                                record.getEmployeeId(),
                                record.getEmployeeName(),
                                record.getJobTitle(),
                                record.getSalary()))
                        .collect(Collectors.toList());
                payrollDTOs.put(deptName, recordDTOs);
            });

            logger.info("Payroll data successfully generated for {} departments", payrollDTOs.size());
            return ResponseEntity.ok(payrollDTOs);

        } catch (Exception e) {
            logger.error("Error generating department-wise payroll: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(null); // Internal Server Error
        }
    }
}
