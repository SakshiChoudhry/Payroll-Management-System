package com.epam.campusTrainings.employeePayroll.mappers;


import com.epam.campusTrainings.employeePayroll.dto.DepartmentDTO;
import com.epam.campusTrainings.employeePayroll.model.Department;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    // Convert Department entity to DepartmentDTO
    DepartmentDTO toDto(Department department);

    // Convert list of Department entities to list of DepartmentDTO objects
    List<DepartmentDTO> listToDto(List<Department> departments);

    // Convert DepartmentDTO to Department entity
    Department toEntity(DepartmentDTO departmentDTO);
}
