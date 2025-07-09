package com.epam.campusTrainings.employeePayroll.model;

import jakarta.persistence.*;

@Entity
@Table(name="employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int employeeId;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "dept_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_department"))
    private Department department;  // JPA manages dept_id in DB

    @ManyToOne
    @JoinColumn(name = "job_title_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_job_title"))
    private JobTitle jobTitle;   // JPA manages job_title_id in DB

    @Column
    private String email;

    protected Employee() { } // Required for JPA


    public Employee(String name,Department department,JobTitle jobTitle, String email){
        this.employeeId=0;    //JPA will override this
        this.name=name;
        this.department=department;
        this.jobTitle=jobTitle;
        this.email=email;

    }


    //Getters
    public int getEmployeeId(){
        return employeeId;
    }
    public String getName(){
        return name;
    }

    public JobTitle getJobTitle(){
        return jobTitle;
    }

    public Department getDepartment(){
        return department;
    }

    public int getDeptId() {
        return department.getId();
    }
    public int getJobTitleId() {
        return jobTitle.getId();
    }

    public String getEmail() {
        return email;
    }

    //Setters
    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name;
    }

    public void setDepartment(Department department) {
        if (department == null) {
            throw new IllegalArgumentException("Department cannot be null");
        }
        this.department = department;
    }

    public void setJobTitle(JobTitle jobTitle) {
        if (jobTitle == null) {
            throw new IllegalArgumentException("Job Title cannot be null");
        }
        this.jobTitle = jobTitle;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}