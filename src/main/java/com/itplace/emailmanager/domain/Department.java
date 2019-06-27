package com.itplace.emailmanager.domain;

import javax.persistence.Entity;

@Entity
public class Department extends BaseIdentifierEntity {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
