package com.itplace.emailmanager.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Addressee extends BaseIdentifierEntity {
    private String name;

    private String email;

    @ManyToOne
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
