package com.itplace.emailmanager.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Mail extends BaseIdentifierEntity {

    private String subject;
    @ManyToMany
    private List<Addressee> addressee;
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
        this.status = status;
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

    public void setDelivered(){
        this.setStatus(STATUS.DELIVERED);
    }

    enum STATUS {
        NEW,
        SENDING,
        DELIVERED,
    }
}
