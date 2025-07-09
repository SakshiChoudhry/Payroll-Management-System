package com.epam.campusTrainings.employeePayroll.mappers;

import com.epam.campusTrainings.employeePayroll.dto.JobTitleDTO;
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
public class JobTitleMapperImpl implements JobTitleMapper {

    @Override
    public JobTitleDTO toDto(JobTitle jobTitle) {
        if ( jobTitle == null ) {
            return null;
        }

        JobTitleDTO jobTitleDTO = new JobTitleDTO();

        jobTitleDTO.setId( jobTitle.getId() );
        jobTitleDTO.setTitle( jobTitle.getTitle() );
        jobTitleDTO.setBaseSalary( jobTitle.getBaseSalary() );

        return jobTitleDTO;
    }

    @Override
    public List<JobTitleDTO> listToDto(List<JobTitle> jobTitles) {
        if ( jobTitles == null ) {
            return null;
        }

        List<JobTitleDTO> list = new ArrayList<JobTitleDTO>( jobTitles.size() );
        for ( JobTitle jobTitle : jobTitles ) {
            list.add( toDto( jobTitle ) );
        }

        return list;
    }

    @Override
    public JobTitle toEntity(JobTitleDTO jobTitleDTO) {
        if ( jobTitleDTO == null ) {
            return null;
        }

        String title = null;
        double baseSalary = 0.0d;

        title = jobTitleDTO.getTitle();
        baseSalary = jobTitleDTO.getBaseSalary();

        JobTitle jobTitle = new JobTitle( title, baseSalary );

        return jobTitle;
    }
}
