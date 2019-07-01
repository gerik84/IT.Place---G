package com.itplace.emailmanager.domain;

import com.opencsv.bean.CsvBindByPosition;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Addressee extends BaseIdentifierEntity {
    @CsvBindByPosition(position = 0)
    private String name;
    @CsvBindByPosition(position = 1)
    private String email;
    @ManyToOne
    @CsvBindByPosition(position = 2)
    private Department department;

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
