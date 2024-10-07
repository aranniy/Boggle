package com.example.restservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailMessage;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {

    @Autowired
    private MailSender mailSender;

    @Autowired
    private MailMessage mailMessage;


    public void sendEmail(final String message, final String[] emailAddresses) {
        String subject = "Boggle Game";
        mailMessage.setSubject(subject);
        mailMessage.setTo(emailAddresses);
        mailMessage.setText(message);
        try {
            mailSender.send((SimpleMailMessage) mailMessage);
            System.out.println("Email sending complete.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}