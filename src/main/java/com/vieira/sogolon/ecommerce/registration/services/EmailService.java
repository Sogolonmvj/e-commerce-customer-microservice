package com.vieira.sogolon.ecommerce.registration.services;

import com.vieira.sogolon.ecommerce.registration.sender.EmailSender;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
public class EmailService implements EmailSender {

    private final static Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;

    @Override
    @Async
    public void send(String to, String email) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject("Confirme seu email!");
            helper.setFrom("sogolonmd@gmail.com");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            LOGGER.error("Falha no envio do email!", e);
            throw new IllegalStateException("Falha no envio do email!");
        }
    }

}
