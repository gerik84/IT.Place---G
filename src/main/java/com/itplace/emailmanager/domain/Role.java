package com.itplace.emailmanager.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)

@Entity
public class Role {
    @Id
    @Enumerated(EnumType.STRING)
    private ROLE name;

    public enum ROLE {
        ROLE_USER,
        ROLE_ADMIN
    }
}
