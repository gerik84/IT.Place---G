package com.itplace.emailmanager.util;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageWrapper {
    @JsonManagedReference
    private List<String> ids;
    @JsonManagedReference
    private String messageSubject;
    @JsonManagedReference
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
