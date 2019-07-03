package com.itplace.emailmanager.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)

@Entity
public class MailTask extends BaseIdentifierEntity {
    private Long startTime; // время первого выполнения в мс
    @Enumerated(EnumType.STRING)
    private PERIOD period = PERIOD.ONCE;
    @OneToOne
    private Mail mail;
    @Enumerated(EnumType.STRING)
    private STATUS status = STATUS.NEW;

    public enum STATUS {
        NEW,
        IN_PROGRESS,
        PAUSED,
        DONE
    }

    public enum PERIOD {
        ONCE,
        DAILY,
        WEEKLY,
        MONTHLY,
        YEARLY
    }
}
