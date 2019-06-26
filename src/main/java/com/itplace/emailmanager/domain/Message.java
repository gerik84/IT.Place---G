package com.itplace.emailmanager.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
public class Message extends BaseIdentifierEntity {

    @ManyToMany
    private List<LocalizedString> text;

    public List<LocalizedString> getText() {
        return text;
    }

    public void setText(List<LocalizedString> text) {
        this.text = text;
    }
}

