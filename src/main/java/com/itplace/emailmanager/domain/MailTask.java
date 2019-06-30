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
    private Long intervalTime;
    private Integer repeatsLeft;
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
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getIntervalTime() {
        return intervalTime;
    }

    /**
     *
     * @param intervalTime интервал между выполнениями в мс
     */
    public void setIntervalTime(long intervalTime) {
        this.intervalTime = intervalTime;
    }

    public Integer getRepeatsLeft() {
        return repeatsLeft;
    }

    /**
     * @param repeatsLeft используем -1 для создания бесконечной задачи
     */
    public void setRepeatsLeft(Integer repeatsLeft) {
        this.repeatsLeft = repeatsLeft;
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

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public enum STATUS {
        NEW,
        IN_PROGRESS,
        DONE
    }
}
