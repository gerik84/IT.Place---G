package com.itplace.emailmanager.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)

@Entity
public class Addressee extends BaseIdentifierEntity {
    private String name;
    private String email;
    @ManyToOne
    private Department department;
}
