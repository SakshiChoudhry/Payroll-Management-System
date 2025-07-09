package com.epam.campusTrainings.employeePayroll.mappers;

import com.epam.campusTrainings.employeePayroll.dto.EmployeeDTO;
import com.epam.campusTrainings.employeePayroll.model.Department;
import com.epam.campusTrainings.employeePayroll.model.Employee;
import com.epam.campusTrainings.employeePayroll.model.JobTitle;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-26T20:31:12+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class EmployeeMapperImpl implements EmployeeMapper {

    @Override
    public EmployeeDTO toDto(Employee employee) {
        if ( employee == null ) {
            return null;
        }

        EmployeeDTO employeeDTO = new EmployeeDTO();

        employeeDTO.setDepartmentId( employee.getDeptId() );
        employeeDTO.setJobTitleId( employee.getJobTitleId() );
        employeeDTO.setEmployeeId( employee.getEmployeeId() );
        employeeDTO.setName( employee.getName() );
        employeeDTO.setEmail( employee.getEmail() );

        return employeeDTO;
    }

    @Override
    public List<EmployeeDTO> listToDto(List<Employee> employees) {
        if ( employees == null ) {
            return null;
        }

        List<EmployeeDTO> list = new ArrayList<EmployeeDTO>( employees.size() );
        for ( Employee employee : employees ) {
            list.add( toDto( employee ) );
        }

        return list;
    }

    @Override
    public void updateEmployeeEntity(Employee employee, EmployeeDTO employeeDTO) {
        if ( employeeDTO == null ) {
            return;
        }

        employee.setName( employeeDTO.getName() );
        employee.setEmail( employeeDTO.getEmail() );
    }

    @Override
    public Employee toEntity(EmployeeDTO employeeDTO) {
        if ( employeeDTO == null ) {
            return null;
        }

        String name = null;
        String email = null;

        name = employeeDTO.getName();
        email = employeeDTO.getEmail();

        Department department = null;
        JobTitle jobTitle = null;

        Employee employee = new Employee( name, department, jobTitle, email );

        return employee;
    }
}
