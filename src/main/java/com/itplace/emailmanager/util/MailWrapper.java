package com.itplace.emailmanager.util;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MailWrapper {
    private List<String> messageAddressees;
    private String messageSubject;
    private String messageText;

    public MailWrapper() {
    }

    public List<String> getMessageAddressees() {
        return messageAddressees;
    }

    public void setMessageAddressees(List<String> messageAddressees) {
        this.messageAddressees = messageAddressees;
    }

    public String getMessageSubject() {
        return messageSubject;
    }

    public void setMessageSubject(String messageSubject) {
        this.messageSubject = messageSubject;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}
