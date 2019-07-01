package com.itplace.emailmanager.domain;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity {
    private Long whenCreated;
    private Long whenUpdated;
    private Long whenDeleted = null;

    public BaseEntity() {
        setWhenCreated(System.currentTimeMillis());
    }

    public Long getWhenCreated() {
        return whenCreated;
    }

    public void setWhenCreated(Long whenCreated) {
        this.whenCreated = whenCreated;
        this.setWhenUpdated(whenCreated);
    }

    public Long getWhenUpdated() {
        return whenUpdated;
    }

    public void setWhenUpdated(Long whenUpdated) {
        this.whenUpdated = whenUpdated;
    }

    public Long getWhenDeleted() {
        return whenDeleted;
    }

    public void setWhenDeleted(Long whenDeleted) {
        this.whenDeleted = whenDeleted;
    }
}
