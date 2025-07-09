package com.epam.campusTrainings.employeePayroll;

import com.epam.campusTrainings.employeePayroll.dto.DepartmentDTO;
import com.epam.campusTrainings.employeePayroll.dto.EmployeeDTO;
import com.epam.campusTrainings.employeePayroll.dto.JobTitleDTO;
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
import com.epam.campusTrainings.employeePayroll.service.PayrollService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PayrollServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private JobTitleRepository jobTitleRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private DepartmentMapper departmentMapper;

    @Mock
    private JobTitleMapper jobTitleMapper;

    @InjectMocks
    private PayrollService payrollService;

    private Department testDepartment;
    private JobTitle testJobTitle;
    private Employee testEmployee;
    private List<Employee> employeeList;
    private List<Department> departmentList;
    private List<JobTitle> jobTitleList;

    @BeforeEach
    void setUp() {
        // Setup test data
        testDepartment = new Department("Engineering");


        testJobTitle = new JobTitle("Software Engineer", 100000.0);

        testEmployee = new Employee("John Doe", testDepartment, testJobTitle, "johndoe@gmail.com");

        employeeList = new ArrayList<>(Arrays.asList(testEmployee));
        departmentList = new ArrayList<>(Arrays.asList(testDepartment));
        jobTitleList = new ArrayList<>(Arrays.asList(testJobTitle));
    }

    // ============== CREATE METHODS TESTS ==============

    @Test
    void testCreateEmployee_Success() {
        // Arrange
        when(departmentRepository.findById(anyInt())).thenReturn(Optional.of(testDepartment));
        when(jobTitleRepository.findById(anyInt())).thenReturn(Optional.of(testJobTitle));

        // Act
        payrollService.createEmployee("John Doe", 1, 1,"johnDoe@gmail.com");

        // Assert
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void testCreateEmployee_DepartmentNotFound() {
        // Arrange
        when(departmentRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(DepartmentNotFoundException.class,
                () -> payrollService.createEmployee("John Doe", 1, 1,"johnDoe@gamil.com"));
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void testCreateEmployee_JobTitleNotFound() {
        // Arrange
        when(departmentRepository.findById(anyInt())).thenReturn(Optional.of(testDepartment));
        when(jobTitleRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(JobTitleNotFoundException.class,
                () -> payrollService.createEmployee("John Doe", 1, 1,"johnDoes@gmail.com"));
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void testCreateDepartment_Success() {
        // Arrange
        when(departmentRepository.existsByNameIgnoreCase(anyString())).thenReturn(false);

        // Act
        payrollService.createDepartment("Engineering");

        // Assert
        verify(departmentRepository, times(1)).save(any(Department.class));
    }

    @Test
    void testCreateDepartment_AlreadyExists() {
        // Arrange
        when(departmentRepository.existsByNameIgnoreCase(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> payrollService.createDepartment("Engineering"));
        verify(departmentRepository, never()).save(any(Department.class));
    }

    @Test
    void testCreateJobTitle_Success() {
        // Arrange
        when(jobTitleRepository.existsByTitleIgnoreCase(anyString())).thenReturn(false);

        // Act
        payrollService.createJobTitle("Software Engineer", 100000.0);

        // Assert
        verify(jobTitleRepository, times(1)).save(any(JobTitle.class));
    }

    @Test
    void testCreateJobTitle_AlreadyExists() {
        // Arrange
        when(jobTitleRepository.existsByTitleIgnoreCase(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> payrollService.createJobTitle("Software Engineer", 100000.0));
        verify(jobTitleRepository, never()).save(any(JobTitle.class));
    }

    // ============== READ METHODS TESTS ==============

    @Test
    void testGetAllEmployeeEntities_Success() {
        // Arrange
        when(employeeRepository.findAll()).thenReturn(employeeList);

        // Act
        List<Employee> result = payrollService.getAllEmployeeEntities();

        // Assert
        assertEquals(employeeList.size(), result.size());
        assertEquals(employeeList, result);
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void testGetAllEmployees_Success() {
        // Arrange
        when(employeeRepository.findAll()).thenReturn(employeeList);
        when(employeeMapper.listToDto(anyList())).thenReturn(Collections.emptyList());

        // Act
        payrollService.getAllEmployees();

        // Assert
        verify(employeeRepository, times(1)).findAll();
        verify(employeeMapper, times(1)).listToDto(employeeList);
    }

    @Test
    void testGetAllDepartments_Success() {
        // Arrange
        when(departmentRepository.findAll()).thenReturn(departmentList);
        when(departmentMapper.listToDto(anyList())).thenReturn(Collections.emptyList());

        // Act
        payrollService.getAllDepartments();

        // Assert
        verify(departmentRepository, times(1)).findAll();
        verify(departmentMapper, times(1)).listToDto(departmentList);
    }

    @Test
    void testGetAllJobTitles_Success() {
        // Arrange
        when(jobTitleRepository.findAll()).thenReturn(jobTitleList);
        when(jobTitleMapper.listToDto(anyList())).thenReturn(Collections.emptyList());

        // Act
        payrollService.getAllJobTitles();

        // Assert
        verify(jobTitleRepository, times(1)).findAll();
        verify(jobTitleMapper, times(1)).listToDto(jobTitleList);
    }

    @Test
    void testGetEmployeeById_Success() {
        // Arrange
        when(employeeRepository.findById(anyInt())).thenReturn(Optional.of(testEmployee));
        when(employeeMapper.toDto(any(Employee.class))).thenReturn(new EmployeeDTO());

        // Act
        payrollService.getEmployeeById(1);

        // Assert
        verify(employeeRepository, times(1)).findById(1);
        verify(employeeMapper, times(1)).toDto(testEmployee);
    }

    @Test
    void testGetEmployeeById_NotFound() {
        // Arrange
        when(employeeRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EmployeeNotFoundException.class, () -> payrollService.getEmployeeById(1));
        verify(employeeRepository, times(1)).findById(1);
        verify(employeeMapper, never()).toDto(any(Employee.class));
    }

    @Test
    void testGetDepartmentById_Success() {
        // Arrange
        when(departmentRepository.findById(anyInt())).thenReturn(Optional.of(testDepartment));
        when(departmentMapper.toDto(any(Department.class))).thenReturn(new DepartmentDTO());

        // Act
        payrollService.getDepartmentById(1);

        // Assert
        verify(departmentRepository, times(1)).findById(1);
        verify(departmentMapper, times(1)).toDto(testDepartment);
    }

    @Test
    void testGetDepartmentById_NotFound() {
        // Arrange
        when(departmentRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(DepartmentNotFoundException.class, () -> payrollService.getDepartmentById(1));
        verify(departmentRepository, times(1)).findById(1);
        verify(departmentMapper, never()).toDto(any(Department.class));
    }

    @Test
    void testGetJobTitleById_Success() {
        // Arrange
        when(jobTitleRepository.findById(anyInt())).thenReturn(Optional.of(testJobTitle));
        when(jobTitleMapper.toDto(any(JobTitle.class))).thenReturn(new JobTitleDTO());

        // Act
        payrollService.getJobTitleById(1);

        // Assert
        verify(jobTitleRepository, times(1)).findById(1);
        verify(jobTitleMapper, times(1)).toDto(testJobTitle);
    }

    @Test
    void testGetJobTitleById_NotFound() {
        // Arrange
        when(jobTitleRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(JobTitleNotFoundException.class, () -> payrollService.getJobTitleById(1));
        verify(jobTitleRepository, times(1)).findById(1);
        verify(jobTitleMapper, never()).toDto(any(JobTitle.class));
    }

    // ============== UPDATE METHODS TESTS ==============

    @Test
    void testUpdateEmployee_Success() {
        // Arrange
        when(employeeRepository.findById(anyInt())).thenReturn(Optional.of(testEmployee));
        when(departmentRepository.findById(anyInt())).thenReturn(Optional.of(testDepartment));
        when(jobTitleRepository.findById(anyInt())).thenReturn(Optional.of(testJobTitle));
        when(employeeRepository.save(any(Employee.class))).thenReturn(testEmployee);

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName("Updated Name");
        employeeDTO.setDepartmentId(1);
        employeeDTO.setJobTitleId(1);

        // Act
        Employee result = payrollService.updateEmployee(1, employeeDTO);

        // Assert
        assertNotNull(result);
        verify(employeeRepository, times(1)).findById(1);
        verify(departmentRepository, times(1)).findById(1);
        verify(jobTitleRepository, times(1)).findById(1);
        verify(employeeMapper, times(1)).updateEmployeeEntity(eq(testEmployee), eq(employeeDTO));
        verify(employeeRepository, times(1)).save(testEmployee);
    }

    @Test
    void testUpdateEmployee_EmployeeNotFound() {
        // Arrange
        when(employeeRepository.findById(anyInt())).thenReturn(Optional.empty());

        EmployeeDTO employeeDTO = new EmployeeDTO();

        // Act & Assert
        assertThrows(EmployeeNotFoundException.class, () -> payrollService.updateEmployee(1, employeeDTO));
        verify(employeeRepository, times(1)).findById(1);
        verify(departmentRepository, never()).findById(anyInt());
        verify(jobTitleRepository, never()).findById(anyInt());
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void testUpdateEmployee_DepartmentNotFound() {
        // Arrange
        when(employeeRepository.findById(anyInt())).thenReturn(Optional.of(testEmployee));
        when(departmentRepository.findById(anyInt())).thenReturn(Optional.empty());

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setDepartmentId(99); // Non-existent department

        // Act & Assert
        assertThrows(DepartmentNotFoundException.class, () -> payrollService.updateEmployee(1, employeeDTO));
        verify(employeeRepository, times(1)).findById(1);
        verify(departmentRepository, times(1)).findById(99);
        verify(jobTitleRepository, never()).findById(anyInt());
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void testUpdateEmployee_JobTitleNotFound() {
        // Arrange
        when(employeeRepository.findById(anyInt())).thenReturn(Optional.of(testEmployee));
        when(departmentRepository.findById(anyInt())).thenReturn(Optional.of(testDepartment));
        when(jobTitleRepository.findById(anyInt())).thenReturn(Optional.empty());

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setDepartmentId(1);
        employeeDTO.setJobTitleId(99); // Non-existent job title

        // Act & Assert
        assertThrows(JobTitleNotFoundException.class, () -> payrollService.updateEmployee(1, employeeDTO));
        verify(employeeRepository, times(1)).findById(1);
        verify(departmentRepository, times(1)).findById(1);
        verify(jobTitleRepository, times(1)).findById(99);
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    // ============== DELETE METHODS TESTS ==============

    @Test
    void testDeleteEmployee_Success() {
        // Arrange
        when(employeeRepository.existsById(anyInt())).thenReturn(true);

        // Act
        payrollService.deleteEmployee(1);

        // Assert
        verify(employeeRepository, times(1)).existsById(1);
        verify(employeeRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteEmployee_NotFound() {
        // Arrange
        when(employeeRepository.existsById(anyInt())).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> payrollService.deleteEmployee(1));
        verify(employeeRepository, times(1)).existsById(1);
        verify(employeeRepository, never()).deleteById(anyInt());
    }

    // ============== BUSINESS LOGIC METHODS TESTS ==============

    @Test
    void testCalculateAverageSalaryByDepartment_Success() {
        // Arrange
        when(departmentRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(testDepartment));
        when(employeeRepository.findByDepartment(any(Department.class))).thenReturn(employeeList);

        // Act
        double result = payrollService.calculateAverageSalaryByDepartment("Engineering");

        // Assert
        assertEquals(100000.0, result);
        verify(departmentRepository, times(1)).findByNameIgnoreCase("Engineering");
        verify(employeeRepository, times(1)).findByDepartment(testDepartment);
    }

    @Test
    void testCalculateAverageSalaryByDepartment_DepartmentNotFound() {
        // Arrange
        when(departmentRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(DepartmentNotFoundException.class,
                () -> payrollService.calculateAverageSalaryByDepartment("Non-existent"));
        verify(departmentRepository, times(1)).findByNameIgnoreCase("Non-existent");
        verify(employeeRepository, never()).findByDepartment(any(Department.class));
    }

    @Test
    void testCalculateAverageSalaryByDepartment_NoEmployees() {
        // Arrange
        when(departmentRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(testDepartment));
        when(employeeRepository.findByDepartment(any(Department.class))).thenReturn(Collections.emptyList());

        // Act
        double result = payrollService.calculateAverageSalaryByDepartment("Engineering");

        // Assert
        assertEquals(0.0, result);
        verify(departmentRepository, times(1)).findByNameIgnoreCase("Engineering");
        verify(employeeRepository, times(1)).findByDepartment(testDepartment);
    }

    @Test
    void testGetTopNHighestPaidEmployees_Success() {
        // Arrange
        Employee employee1 = new Employee("John", testDepartment,
                new JobTitle("Developer", 80000.0),"emp1@gmail.com");
        Employee employee2 = new Employee("Jane", testDepartment,
                new JobTitle("Manager", 100000.0),"emp2@gmail.com");
        Employee employee3 = new Employee("Bob", testDepartment,
                new JobTitle("Director", 120000.0),"emp3@gmail.com");

        List<Employee> employees = Arrays.asList(employee1, employee2, employee3);
        when(employeeRepository.findAll()).thenReturn(employees);
        when(employeeMapper.toDto(any(Employee.class))).thenReturn(new EmployeeDTO());

        // Act
        List<EmployeeDTO> result = payrollService.getTopNHighestPaidEmployees(2);

        // Assert
        assertEquals(2, result.size());
        verify(employeeRepository, times(1)).findAll();
        verify(employeeMapper, times(2)).toDto(any(Employee.class));
    }

}