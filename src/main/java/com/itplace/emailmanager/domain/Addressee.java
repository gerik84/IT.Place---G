package com.itplace.emailmanager.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)

@Entity
public class Addressee extends BaseIdentifierEntity {

    @NotNull
    private String name;

    @NotNull
    @Email
    private String email;

    @ManyToOne
    private Department department;
}
