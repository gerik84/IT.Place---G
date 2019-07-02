package com.itplace.emailmanager.util;

import com.itplace.emailmanager.domain.Sender;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.stereotype.Component;

@Component
public class SmtpConnectionTester {
    // пока ничего лучше не придумал, методы без отправки письма с Яндексом не работают
    public boolean isConnectionOk(Sender sender) {
        try {
            Email email = new SimpleEmail();
            email.setHostName(sender.getSmtp());
            email.setSmtpPort(sender.getPort());
            email.setAuthenticator(new DefaultAuthenticator(sender.getEmail(), sender.getEmailPassword()));
            email.setSSLOnConnect(true);
            email.setFrom(sender.getEmail());
            email.setSubject("Подтверждение подключения к SMTP серверу.");
            email.setMsg("Проверяем подключение к почтовому серверу.\nДанное письмо отправлено автоматически.");
            email.addTo(sender.getEmail());
            email.send();
            return true;
        } catch (EmailException e) {
            return false;
        }
    }
}