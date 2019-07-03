package com.itplace.emailmanager.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)

@Entity
public class Sender extends BaseIdentifierEntity {
    private String email;
    private String password;
    private String emailPassword;
    private Integer port;
    private String smtp;
    @ManyToOne
    private Role role;
}
