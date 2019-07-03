package com.itplace.emailmanager.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.MappedSuperclass;

@Data
@AllArgsConstructor

@MappedSuperclass
public abstract class BaseEntity {
    private Long whenCreated;
    private Long whenUpdated;
    private Long whenDeleted = null;

    public BaseEntity() {
        setWhenCreated(System.currentTimeMillis());
    }

    public void setWhenCreated(Long whenCreated) {
        this.whenCreated = whenCreated;
        this.setWhenUpdated(whenCreated);
    }

    public void setWhenUpdated(Long whenUpdated) {
        this.whenUpdated = whenUpdated;
    }
}
