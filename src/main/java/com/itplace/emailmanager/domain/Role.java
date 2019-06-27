package com.itplace.emailmanager.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
public class Role {
    @Id
    @Enumerated(EnumType.STRING)
    private ROLE name;

    public ROLE getName() {
        return name;
    }

    public void setName(ROLE name) {
        this.name = name;
    }

    public enum ROLE {
        ROLE_USER,
        ROLE_ADMIN
    }
}
