package com.itplace.emailmanager.util;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageWrapper {
    private List<String> ids;
    private String messageSubject;
    private String messageText;

    public MessageWrapper() {
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
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
