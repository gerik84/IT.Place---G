package com.itplace.emailmanager.domain;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)

@Entity
public class Mail extends BaseIdentifierEntity {
    private String subject;
    @Lob
    private String message;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Addressee> addressee;
    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<MailLog> mailLog;
    @ManyToOne
    private Sender sender;
    @OneToOne
    private MailTask mailTask;
    @Enumerated(EnumType.STRING)
    private STATUS status = STATUS.NEW;

    public enum STATUS {
        NEW,
        SENDING,
        SENT,
        FAILED
    }
}
