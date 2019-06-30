package com.itplace.emailmanager.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Mail extends BaseIdentifierEntity {
    private String subject;
    private String message;
    private Integer attempts = 0;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Addressee> addressee;
    @ManyToOne
    private Sender sender;
    @OneToOne
    private MailTask mailTask;
    @Enumerated(EnumType.STRING)
    private STATUS status = STATUS.NEW;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<Addressee> getAddressee() {
        return addressee;
    }

    public void setAddressee(List<Addressee> addressee) {
        this.addressee = addressee;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        if (status == STATUS.NEW) attempts = 0;
        this.status = status;
    }

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MailTask getMailTask() {
        return mailTask;
    }

    public void setMailTask(MailTask mailTask) {
        this.mailTask = mailTask;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public enum STATUS {
        NEW,
        READY,
        SENDING,
        SENT,
        FAILED,
        CANCELLED
    }
}
