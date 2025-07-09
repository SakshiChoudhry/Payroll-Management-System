package com.epam.campusTrainings.employeePayroll.model;

import jakarta.persistence.*;

@Entity
@Table(name = "jobtitles")
public class JobTitle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private double baseSalary;

    protected JobTitle() { }

    public JobTitle(String title,double baseSalary){
        this.id=0;
        this.title=title;
        this.baseSalary=baseSalary;
    }

    //Getters
    public int getId(){
        return id;
    }
    public String getTitle(){
        return title;
    }
    public double getBaseSalary(){
        return baseSalary;
    }

    //Setters
    public void setTitle(String title){
        if(title==null|| title.isEmpty()){
            throw new IllegalArgumentException("Invalid name input");
        }
        this.title=title;
    }

    public void setBaseSalary(double baseSalary) {
        if(baseSalary<=0){
            throw new IllegalArgumentException("Invalid salary input");
        }
        this.baseSalary = baseSalary;
    }

    public double calculateSalary(double baseSalary){
        return baseSalary-(baseSalary*0.10);         //Giving tax deduction of 10%
    }
}