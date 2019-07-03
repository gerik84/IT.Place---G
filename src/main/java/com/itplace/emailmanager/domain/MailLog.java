package com.itplace.emailmanager.domain;

import lombok.*;

import javax.persistence.Entity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)

@Entity
public class MailLog extends BaseIdentifierEntity {
    private Mail.STATUS mailStatus;
    private String message;
}
