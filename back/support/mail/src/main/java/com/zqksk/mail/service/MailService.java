package com.zqksk.mail.service;

import com.zqksk.mail.config.MailQueueHolder;
import com.zqksk.mail.model.Mail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender mailSender;
    private final MailQueueHolder mailQueueHolder;

    public MailService(JavaMailSender mailSender, MailQueueHolder mailQueueHolder) {
        this.mailSender = mailSender;
        this.mailQueueHolder = mailQueueHolder;
    }

    @Async
    public void sendAuthenticationCode(final Mail mail) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("admin@gmail.com");
            helper.setTo(mail.getRecipient());
            helper.setSubject(mail.getSubject());
            helper.setText(mail.getContent(), true);
            helper.addInline("logoImage", new ClassPathResource("images/logo.png"));

            mailSender.send(message);
        } catch (MailException | MessagingException e) {
            mailQueueHolder.getSharedMailQueue().add(mail);
        }
    }
}
