package com.itplace.emailmanager.domain;

import lombok.*;

import javax.persistence.Entity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)

@Entity
public class Department extends BaseIdentifierEntity {
    private String name;
}
