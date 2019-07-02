package com.itplace.emailmanager.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;

/**
 * Сущность планировщика для письма.
 * Варианты инициализации:
 *  startTime = время выполнения, intervalTime = 0, repeatsLeft = 1 -- одноразовое письмо;
 *  startTime = время выполнения, intervalTime = интервал, repeatsLeft = N -- письмо отправится N раз;
 *  startTime = время выполнения, intervalTime = интервал, repeatsLeft = -1 -- письмо будет отправляться через интервал N бесконечно (для приостановки - STATUS.DONE)
 *  Если планировщик = null при создании письма, используется первый вариант.
 */

@Entity
public class MailTask extends BaseIdentifierEntity {
    private Long startTime;
    @Enumerated(EnumType.STRING)
    private PERIOD period = PERIOD.ONCE;
    @OneToOne
    private Mail mail;
    @Enumerated(EnumType.STRING)
    private STATUS status = STATUS.NEW;

    public long getStartTime() {
        return startTime;
    }

    /**
     *
     * @param startTime время первого выполнения в мс
     */
    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public PERIOD getPeriod() {
        return period;
    }

    public void setPeriod(PERIOD period) {
        this.period = period;
    }

    public Mail getMail() {
        return mail;
    }

    public void setMail(Mail mail) {
        this.mail = mail;
    }

    public STATUS getStatus() {
        return status;
    }

    /**
     * @param status NEW - новая задача; IN_PROGRESS - выполняется; DONE - выполнена; CANCELLED - отменена
     *
     */
    public void setStatus(STATUS status) {
        this.status = status;
    }

    public enum STATUS {
        NEW,
        IN_PROGRESS,
        DONE,
        CANCELLED
    }

    public enum PERIOD {
        ONCE,
        DAILY,
        WEEKLY,
        MONTHLY,
        YEARLY
    }
}
