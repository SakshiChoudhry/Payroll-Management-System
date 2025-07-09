package com.epam.campusTrainings.employeePayroll.mappers;

import com.epam.campusTrainings.employeePayroll.dto.DepartmentDTO;
import com.epam.campusTrainings.employeePayroll.model.Department;
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
public class DepartmentMapperImpl implements DepartmentMapper {

    @Override
    public DepartmentDTO toDto(Department department) {
        if ( department == null ) {
            return null;
        }

        DepartmentDTO departmentDTO = new DepartmentDTO();

        departmentDTO.setId( department.getId() );
        departmentDTO.setName( department.getName() );

        return departmentDTO;
    }

    @Override
    public List<DepartmentDTO> listToDto(List<Department> departments) {
        if ( departments == null ) {
            return null;
        }

        List<DepartmentDTO> list = new ArrayList<DepartmentDTO>( departments.size() );
        for ( Department department : departments ) {
            list.add( toDto( department ) );
        }

        return list;
    }

    @Override
    public Department toEntity(DepartmentDTO departmentDTO) {
        if ( departmentDTO == null ) {
            return null;
        }

        String name = null;

        name = departmentDTO.getName();

        Department department = new Department( name );

        return department;
    }
}
