package com.itplace.emailmanager.domain;

import javax.persistence.Entity;

@Entity
public class MailLog extends BaseIdentifierEntity {
    private Mail.STATUS mailStatus;
    private String message;

    public Mail.STATUS getMailStatus() {
        return mailStatus;
    }

    public void setMailStatus(Mail.STATUS mailStatus) {
        this.mailStatus = mailStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
