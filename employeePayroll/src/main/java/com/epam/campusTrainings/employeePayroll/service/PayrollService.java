    package com.epam.campusTrainings.employeePayroll.service;

    import com.epam.campusTrainings.employeePayroll.dto.DepartmentDTO;
    import com.epam.campusTrainings.employeePayroll.dto.EmployeeDTO;
    import com.epam.campusTrainings.employeePayroll.dto.JobTitleDTO;
    import com.epam.campusTrainings.employeePayroll.dto.request.RegisterRequest;
    import com.epam.campusTrainings.employeePayroll.exceptions.DepartmentNotFoundException;
    import com.epam.campusTrainings.employeePayroll.exceptions.EmployeeNotFoundException;
    import com.epam.campusTrainings.employeePayroll.exceptions.JobTitleNotFoundException;
    import com.epam.campusTrainings.employeePayroll.mappers.DepartmentMapper;
    import com.epam.campusTrainings.employeePayroll.mappers.EmployeeMapper;
    import com.epam.campusTrainings.employeePayroll.mappers.JobTitleMapper;
    import com.epam.campusTrainings.employeePayroll.model.Department;
    import com.epam.campusTrainings.employeePayroll.model.Employee;
    import com.epam.campusTrainings.employeePayroll.model.JobTitle;
    import com.epam.campusTrainings.employeePayroll.repository.DepartmentRepository;
    import com.epam.campusTrainings.employeePayroll.repository.EmployeeRepository;
    import com.epam.campusTrainings.employeePayroll.repository.JobTitleRepository;
    import com.epam.campusTrainings.employeePayroll.utils.ValidationUtils;
    import com.fasterxml.jackson.databind.ObjectMapper;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.util.*;
    import java.util.stream.Collectors;

    @Service
    public class PayrollService {
        private EmployeeRepository employeeRepository;
        private DepartmentRepository departmentRepository;
        private JobTitleRepository jobTitleRepository;
        private EmployeeMapper employeeMapper;
        private DepartmentMapper departmentMapper;
        private JobTitleMapper jobTitleMapper;
        private static final Logger logger = LoggerFactory.getLogger(PayrollService.class);

        @Autowired
        public PayrollService(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository,
                              JobTitleRepository jobTitleRepository, EmployeeMapper employeeMapper,
                              DepartmentMapper departmentMapper, JobTitleMapper jobTitleMapper, ObjectMapper objectMapper){
            this.employeeRepository=employeeRepository;
            this.departmentRepository=departmentRepository;
            this.jobTitleRepository=jobTitleRepository;
            this.employeeMapper=employeeMapper;
            this.departmentMapper=departmentMapper;
            this.jobTitleMapper=jobTitleMapper;
        }


        public void createEmployee(String name, int deptId, int jobTitleId, String email) {
            logger.info("Creating Employee with name: {}", name);
            Department department = departmentRepository.findById(deptId)
                    .orElseThrow(() -> new DepartmentNotFoundException("Department not found"));
            JobTitle jobTitle = jobTitleRepository.findById(jobTitleId)
                    .orElseThrow(() -> new JobTitleNotFoundException("Job Title not found"));

            ValidationUtils.validateName(name,"Employee name");
            ValidationUtils.validateId(deptId,"Department Id");
            ValidationUtils.validateId(jobTitleId,"JobTitle ID");
            Employee employee = new Employee(name, department, jobTitle,email);
            employeeRepository.save(employee);
            logger.info("Employee successfully created");
        }

        public void createDepartment(String name) {
            logger.info("Creating department with name: {}",name);
            if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException("Invalid Input");
            }
            if (departmentRepository.existsByNameIgnoreCase(name)) {
                throw new IllegalArgumentException("Department already exists");
            }

            ValidationUtils.validateName(name,"Department Name");

            Department department = new Department(name);
            departmentRepository.save(department);
            logger.info("Department successfully created");
        }

        public void createJobTitle(String name, double baseSalary) {
            logger.info("Creating job title with name: {}",name);
            if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException("Invalid name input");
            }
            if (baseSalary <= 0) {
                throw new IllegalArgumentException("Invalid base Salary input");
            }
            if (jobTitleRepository.existsByTitleIgnoreCase(name)) {
                throw new IllegalArgumentException("Job Title already exists");
            }

            ValidationUtils.validateName(name,"JobTitle name");
            JobTitle jobTitle = new JobTitle(name,baseSalary);
            jobTitleRepository.save(jobTitle);
            logger.info("Job Title successfully created");
        }

        public void deleteEmployee(int id) {
            logger.info("Deleting Employee with id: {}",id);
            if (!employeeRepository.existsById(id)) {
                throw new IllegalArgumentException("Employee not found");
            }
            employeeRepository.deleteById(id);
            logger.info("Employee successfully deleted");
        }

        public Employee updateEmployee(int id, EmployeeDTO employeeDTO) {
            logger.info("Updating the employee information. Given id: {}",id);
            Employee existingEmployee = getEmployeeEntityById(id);

            Department department = departmentRepository.findById(employeeDTO.getDepartmentId())
                    .orElseThrow(() -> new DepartmentNotFoundException("Department not found with ID: " + employeeDTO.getDepartmentId()));

            JobTitle jobTitle = jobTitleRepository.findById(employeeDTO.getJobTitleId())
                    .orElseThrow(() -> new JobTitleNotFoundException("JobTitle not found with ID: " + employeeDTO.getJobTitleId()));

            // Update the employee entity
            employeeMapper.updateEmployeeEntity(existingEmployee, employeeDTO);
            existingEmployee.setDepartment(department);
            existingEmployee.setJobTitle(jobTitle);

            logger.info("Successfully updated the information");
            return employeeRepository.save(existingEmployee);
        }

        public Map<String, List<PayrollRecord>> generateDepartmentWisePayroll() {
            logger.info("Starting the process of generating the payroll");
            Map<String, List<PayrollRecord>> payrollByDept = new HashMap<>();

            List<Department> departments = departmentRepository.findAll();

            for (Department dept : departments) {
                List<PayrollRecord> departmentPayroll = new ArrayList<>();
                List<Employee> deptEmployees = employeeRepository.findByDepartment(dept);

                for (Employee emp : deptEmployees) {
                    try {
                        JobTitle jobTitle = jobTitleRepository.findById(emp.getJobTitleId())
                                .orElse(new JobTitle("Unknown Position", 0.0));

                        PayrollRecord record = new PayrollRecord(
                                emp.getEmployeeId(),
                                emp.getName(),
                                jobTitle.getTitle(),
                                jobTitle.calculateSalary(jobTitle.getBaseSalary())
                        );
                        departmentPayroll.add(record);
                    } catch (Exception e) {
                        // Create a record with default values instead of crashing
                        PayrollRecord record = new PayrollRecord(
                                emp.getEmployeeId(),
                                emp.getName(),
                                "Position Not Found",
                                0.0
                        );
                        departmentPayroll.add(record);
                    }
                }

                payrollByDept.put(dept.getName(), departmentPayroll);
            }

            logger.info("Successfully created the payroll");
            return payrollByDept;
        }

        public List<DepartmentDTO> getAllDepartments() {
            logger.info("Fetching all the departments from the repository");
            List<Department> departments=departmentRepository.findAll();
            List<DepartmentDTO> departmentDTOS=departmentMapper.listToDto(departments);
            logger.info("Successfully retained all the departments");
            return departmentDTOS;
        }

        public List<JobTitleDTO> getAllJobTitles() {
            logger.info("Fetching all the job titles");
            List<JobTitle> jobTitles= jobTitleRepository.findAll();
            List<JobTitleDTO> jobTitleDTOS=jobTitleMapper.listToDto(jobTitles);
            logger.info("Successfully retained all the job titles");
            return jobTitleDTOS;
        }

        public List<EmployeeDTO> getAllEmployees() {
            logger.info("Fetching all the employees");

            List<Employee>employees=employeeRepository.findAll();
            List<EmployeeDTO>employeeDTOS=employeeMapper.listToDto(employees);
            logger.info("Successfully retained all the job titles");
            return employeeDTOS;

        }
        public List<Employee> getAllEmployeeEntities() {
            logger.info("Fetching all employees from the database");
            List<Employee> employees = employeeRepository.findAll();
            logger.info("Retrieved {} employees", employees.size());
            return employees;
        }

        public boolean isItAValidDepartmentId(int deptId) {
            logger.info("Checking if department ID {} is valid", deptId);
            boolean isValid = departmentRepository.existsById(deptId);
            logger.info("Department ID {} valid: {}", deptId, isValid);
            return isValid;
        }

        public boolean isItAValidJobTitleId(int jobTitleId) {
            logger.info("Checking if job title ID {} is valid", jobTitleId);
            boolean isValid = jobTitleRepository.existsById(jobTitleId);
            logger.info("Job title ID {} valid: {}", jobTitleId, isValid);
            return isValid;
        }

        public boolean departmentAlreadyExists(String name) {
            logger.info("Checking if department with name {} already exists", name);
            boolean exists = departmentRepository.existsByNameIgnoreCase(name);
            logger.info("Department with name {} exists: {}", name, exists);
            return exists;
        }

        public boolean jobTitleAlreadyExists(String name) {
            logger.info("Checking if job title with name {} already exists", name);
            boolean exists = jobTitleRepository.existsByTitleIgnoreCase(name);
            logger.info("Job title with name {} exists: {}", name, exists);
            return exists;
        }

        public boolean isItAValidEmployeeId(int empId) {
            logger.info("Checking if employee ID {} is valid", empId);
            boolean isValid = employeeRepository.existsById(empId);
            logger.info("Employee ID {} valid: {}", empId, isValid);
            return isValid;
        }

        public DepartmentDTO getDepartmentById(int deptId) {
            logger.info("Fetching department with ID {}", deptId);
            Department department = departmentRepository.findById(deptId)
                    .orElseThrow(() -> new DepartmentNotFoundException("Department not found"));
            logger.info("Department found: {}", department.getName());
            return departmentMapper.toDto(department);
        }

        public JobTitleDTO getJobTitleById(int jobTitleId) {
            logger.info("Fetching job title with ID {}", jobTitleId);
            JobTitle jobTitle = jobTitleRepository.findById(jobTitleId)
                    .orElseThrow(() -> new JobTitleNotFoundException("Job Title not found"));
            logger.info("Job title found: {}", jobTitle.getTitle());
            return jobTitleMapper.toDto(jobTitle);
        }

        public EmployeeDTO getEmployeeById(int employeeId) {
            logger.info("Fetching employee with ID {}", employeeId);
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new EmployeeNotFoundException("Employee Id doesn't exist"));
            logger.info("Employee found: {}", employee.getName());
            return employeeMapper.toDto(employee);
        }

        public Employee getEmployeeEntityById(int employeeId) {
            logger.info("Fetching employee entity with ID {}", employeeId);
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new EmployeeNotFoundException("Employee Id doesn't exist"));
            logger.info("Employee entity found: {}", employee.getName());
            return employee;
        }

        public double calculateAverageSalaryByDepartment(String departmentName) {
            logger.info("Calculating average salary for department: {}", departmentName);
            Department department = departmentRepository.findByNameIgnoreCase(departmentName)
                    .orElseThrow(() -> new DepartmentNotFoundException("Department not found: " + departmentName));
            List<Employee> employees = employeeRepository.findByDepartment(department);
            double averageSalary = employees.stream()
                    .mapToDouble(emp -> emp.getJobTitle().getBaseSalary())
                    .average()
                    .orElse(0.0);
            logger.info("Average salary for department {}: {}", departmentName, averageSalary);
            return averageSalary;
        }

        public Map<String, List<EmployeeDTO>> getEmployeesGroupedByDepartment() {
            logger.info("Fetching employees grouped by department");
            List<Employee> employees = employeeRepository.findAll();
            Map<String, List<EmployeeDTO>> groupedEmployees = employees.stream()
                    .collect(Collectors.groupingBy(
                            emp -> emp.getDepartment().getName(),
                            Collectors.mapping(employeeMapper::toDto, Collectors.toList())
                    ));
            logger.info("Grouped employees by department, total groups: {}", groupedEmployees.size());
            return groupedEmployees;
        }

        public List<EmployeeDTO> getTopNHighestPaidEmployees(int n) {
            logger.info("Fetching top {} highest paid employees", n);
            List<EmployeeDTO> topEmployees = employeeRepository.findAll().stream()
                    .sorted(Comparator.comparingDouble((Employee emp) -> emp.getJobTitle().getBaseSalary()).reversed())
                    .limit(n)
                    .map(employeeMapper::toDto)
                    .collect(Collectors.toList());
            logger.info("Retrieved top {} highest paid employees", topEmployees.size());
            return topEmployees;
        }






    }



