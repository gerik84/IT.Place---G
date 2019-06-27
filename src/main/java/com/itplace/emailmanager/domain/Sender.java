package com.itplace.emailmanager.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Sender extends BaseIdentifierEntity {
    private String email;

    private String password;

    private Integer port;

    private String smtp;

    @ManyToOne
    private Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getSmtp() {
        return smtp;
    }

    public void setSmtp(String smtp) {
        this.smtp = smtp;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
