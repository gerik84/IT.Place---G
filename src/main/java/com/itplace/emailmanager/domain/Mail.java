package com.itplace.emailmanager.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

@Entity
public class Mail extends BaseUuidEntity {

    private String subject;
    @ManyToOne
    private Addressee addressee;
    @ManyToOne
    private Sender sender;
    @ManyToOne
    private Message message;
    @Enumerated(EnumType.STRING)
    private STATUS status = STATUS.NEW;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Addressee getAddressee() {
        return addressee;
    }

    public void setAddressee(Addressee addressee) {
        this.addressee = addressee;
    }

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    enum STATUS {
        NEW,
        SENDING,
        DELIVERED,
    }
}
