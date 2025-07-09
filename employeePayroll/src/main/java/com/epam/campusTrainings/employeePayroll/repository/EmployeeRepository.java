package com.epam.campusTrainings.employeePayroll.repository;

import com.epam.campusTrainings.employeePayroll.model.Department;
import com.epam.campusTrainings.employeePayroll.model.Employee;
import com.epam.campusTrainings.employeePayroll.model.JobTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Integer> {
    List<Employee> findByJobTitle(JobTitle jobTitle);
    List<Employee> findByDepartment(Department department);
    Optional<Employee> findByName(String name);

}
