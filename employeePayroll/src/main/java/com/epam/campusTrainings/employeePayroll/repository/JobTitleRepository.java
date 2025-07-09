package com.epam.campusTrainings.employeePayroll.repository;

import com.epam.campusTrainings.employeePayroll.model.JobTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobTitleRepository extends JpaRepository<JobTitle, Integer> {
    boolean existsByTitleIgnoreCase(String title);
    Optional<JobTitle> findByTitleIgnoreCase(String title);
}