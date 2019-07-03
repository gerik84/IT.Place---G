package com.itplace.emailmanager.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)

@MappedSuperclass
public abstract class BaseIdentifierEntity extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
}
