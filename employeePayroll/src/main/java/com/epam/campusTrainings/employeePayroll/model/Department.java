package com.epam.campusTrainings.employeePayroll.model;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

@Entity
@Table(name="departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    protected Department() { }

    public Department(String name){
        this.id=0;
        this.name=name;
    }

    //Getters
    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }

    //Setters
    public void setName(String name){
        if(name==null|| name.isEmpty()){
            throw new IllegalArgumentException("Invalid name input");
        }
        this.name=name;
    }
}