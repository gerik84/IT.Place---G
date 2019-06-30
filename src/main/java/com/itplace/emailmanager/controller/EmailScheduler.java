package com.itplace.emailmanager.controller;

import com.itplace.emailmanager.domain.Mail;
import com.itplace.emailmanager.service.MailService;
import com.itplace.emailmanager.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Component
public class EmailScheduler {
    @Autowired
    private EmailService emailService;
    @Autowired
    private MailService mailService;

    @Scheduled(fixedRateString = "${scheduler.fixed.rate}")
    public void emailScheduler(){
        List<CompletableFuture<Mail>> completableFutures = new ArrayList<>();

        mailService.findMailToSend().forEach(mail -> {
            mail.getAddressee().forEach(addressee -> {
                try {
                    completableFutures.add(emailService.sendMail(addressee.getEmail(), mail));
                } catch (InterruptedException e) { // TODO сделать что-нибудь при возникновении исключения
                }
            });
        });

        CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0])).join();
    }

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(100);
        executor.setMaxPoolSize(100);
        executor.setQueueCapacity(1000);
        executor.setThreadNamePrefix("emailSender-");
        executor.initialize();
        return executor;
    }
}
