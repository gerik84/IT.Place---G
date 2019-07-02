package com.itplace.emailmanager.util;

import com.itplace.emailmanager.domain.Sender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

@Component
public class SmtpConnectionTester {
    @Async
    public CompletableFuture<Sender> checkConnectionStatus(Sender sender){
        boolean connectionOk = isConnectionOk(sender);
        sender.setConnectionOk(connectionOk);

        return CompletableFuture.completedFuture(sender);
    }

    // TODO
    private boolean isConnectionOk(Sender sender) {
        try {
            Properties properties = new Properties();
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.auth", "true");
            Session session = Session.getInstance(properties, null);
            Transport transport = session.getTransport("smtp");
            transport.connect(sender.getSmtp(), sender.getPort(), sender.getEmail(), sender.getEmailPassword());
            transport.close();
            return true;
        } catch (MessagingException e) {
            return false;
        }
    }
}