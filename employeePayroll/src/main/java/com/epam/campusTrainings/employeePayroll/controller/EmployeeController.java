package com.epam.campusTrainings.employeePayroll.controller;

import com.epam.campusTrainings.employeePayroll.dto.EmployeeDTO;
import com.epam.campusTrainings.employeePayroll.dto.request.LoginRequest;
import com.epam.campusTrainings.employeePayroll.dto.request.RegisterRequest;
import com.epam.campusTrainings.employeePayroll.dto.response.RegisterResponse;
import com.epam.campusTrainings.employeePayroll.mappers.EmployeeMapper;
import com.epam.campusTrainings.employeePayroll.service.EmployeeService;
import com.epam.campusTrainings.employeePayroll.service.PayrollService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
@Validated
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    private final PayrollService payrollService;
    private final EmployeeMapper employeeMapper;
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(PayrollService payrollService, EmployeeMapper employeeMapper, EmployeeService employeeService) {
        this.payrollService = payrollService;
        this.employeeMapper = employeeMapper;
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        logger.info("Fetching all employees");
        List<EmployeeDTO> employeeDTOS = payrollService.getAllEmployees();
        logger.info("Retrieved {} employees", employeeDTOS.size());
        return ResponseEntity.ok(employeeDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable int id) {
        logger.info("Fetching employee with id: {}", id);
        if (!payrollService.isItAValidEmployeeId(id)) {
            logger.warn("Employee with id {} not found", id);
            return ResponseEntity.notFound().build();
        }
        EmployeeDTO employeeDTO = payrollService.getEmployeeById(id);
        logger.info("Retrieved employee with id: {}", id);
        return ResponseEntity.ok(employeeDTO);
    }

    @PostMapping
    public ResponseEntity<?> createEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        logger.info("Creating employee with id: {}", employeeDTO.getEmployeeId());
        try {
            payrollService.createEmployee(
                    employeeDTO.getName(),
                    employeeDTO.getDepartmentId(),
                    employeeDTO.getJobTitleId(),
                    employeeDTO.getEmail());
            logger.info("Employee with id {} created successfully", employeeDTO.getEmployeeId());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            logger.error("Failed to create employee: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable int id, @Valid @RequestBody EmployeeDTO employeeDTO) {
        logger.info("Updating employee with id: {}", id);
        if (!payrollService.isItAValidEmployeeId(id)) {
            logger.warn("Employee with id {} not found", id);
            return ResponseEntity.notFound().build();
        }
        payrollService.updateEmployee(id, employeeDTO);
        logger.info("Employee with id {} updated successfully", id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable int id) {
        logger.info("Deleting employee with id: {}", id);
        if (!payrollService.isItAValidEmployeeId(id)) {
            logger.warn("Employee with id {} not found", id);
            return ResponseEntity.notFound().build();
        }
        payrollService.deleteEmployee(id);
        logger.info("Employee with id {} deleted successfully", id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/grouped-by-department")
    public ResponseEntity<Map<String, List<EmployeeDTO>>> getEmployeesGroupedByDepartment() {
        logger.info("Fetching employees grouped by department");
        Map<String, List<EmployeeDTO>> employeesByDepartment = payrollService.getEmployeesGroupedByDepartment();
        logger.info("Retrieved {} departments with grouped employees", employeesByDepartment.size());
        return ResponseEntity.ok(employeesByDepartment);
    }

    @GetMapping("/top-salaries/{n}")
    public ResponseEntity<List<EmployeeDTO>> getTopNHighestPaidEmployees(@PathVariable @Min(1) int n) {
        logger.info("Fetching top {} highest paid employees", n);
        List<EmployeeDTO> topNHighestPaidEmployees = payrollService.getTopNHighestPaidEmployees(n);
        logger.info("Retrieved {} highest paid employees", topNHighestPaidEmployees.size());
        return ResponseEntity.ok(topNHighestPaidEmployees);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse>register(@RequestBody RegisterRequest registerRequest){
        this.employeeService.register(registerRequest);
        return new ResponseEntity<>(new RegisterResponse(true,"Registration successful",registerRequest.getName(),registerRequest.getEmail(), LocalDateTime.now()),HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest){
        String token=this.employeeService.login(loginRequest);
        return ResponseEntity.ok(token);
    }
}
