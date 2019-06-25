package com.itplace.emailmanager.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class LocalizedString extends BaseUuidEntity {

    @Column(length = 2)
    private String locale = "ru";

    @Column(length = 1024)
    private String value;

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
