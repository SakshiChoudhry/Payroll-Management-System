package com.epam.campusTrainings.employeePayroll.mappers;


import com.epam.campusTrainings.employeePayroll.dto.JobTitleDTO;
import com.epam.campusTrainings.employeePayroll.model.JobTitle;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JobTitleMapper {

    // Convert JobTitle entity to JobTitleDTO
    JobTitleDTO toDto(JobTitle jobTitle);

    // Convert list of JobTitle entities to list of JobTitleDTO objects
    List<JobTitleDTO> listToDto(List<JobTitle> jobTitles);

    // Convert JobTitleDTO to JobTitle entity
    JobTitle toEntity(JobTitleDTO jobTitleDTO);
}
