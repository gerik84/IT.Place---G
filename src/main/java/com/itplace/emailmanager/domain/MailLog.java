package com.itplace.emailmanager.domain;

import javax.persistence.Entity;

@Entity
public class MailLog extends BaseIdentifierEntity {
    private Mail.STATUS mailStatus;

    public Mail.STATUS getMailStatus() {
        return mailStatus;
    }

    public void setMailStatus(Mail.STATUS mailStatus) {
        this.mailStatus = mailStatus;
    }
}
