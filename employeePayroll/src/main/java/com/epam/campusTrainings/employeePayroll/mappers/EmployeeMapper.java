package com.epam.campusTrainings.employeePayroll.mappers;

import com.epam.campusTrainings.employeePayroll.dto.EmployeeDTO;
import com.epam.campusTrainings.employeePayroll.dto.request.RegisterRequest;
import com.epam.campusTrainings.employeePayroll.model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    // Convert employee entity to employee DTO
    @Mapping(source = "deptId", target = "departmentId")
    @Mapping(source = "jobTitleId", target = "jobTitleId")
    EmployeeDTO toDto(Employee employee);

    // Convert the list of Employee entities to a list of EmployeeDTOs
    List<EmployeeDTO> listToDto(List<Employee> employees);

    // Update existing Employee entity from EmployeeDTO
    @Mapping(target = "employeeId", ignore = true) // Prevent ID modification
    void updateEmployeeEntity(@MappingTarget Employee employee, EmployeeDTO employeeDTO);

    // Create new Employee entity from EmployeeDTO
    @Mapping(target = "employeeId", ignore = true) // ID is auto-generated
    Employee toEntity(EmployeeDTO employeeDTO);

//    // Convert RegisterRequest to User entity
//    @Mapping(target = "employeeId", ignore = true) // ID is auto-generated
//    @Mapping(target = "password", ignore = true) // Password will be encoded separately
//    Employee toEntity(RegisterRequest registerRequest);
//
//    // Update existing User entity from RegisterRequest
//    @Mapping(target = "employeeId", ignore = true) // Prevent ID modification
//    @Mapping(target = "password", ignore = true) // Password will be encoded separately
//    void updateUserEntity(@MappingTarget Employee employee, RegisterRequest registerRequest);
//
////    // Convert User entity to AuthResponse
////    @Mapping(target = "token", ignore = true) // Token will be set separately
////    AuthResponse toAuthResponse(Employee employee);
}
