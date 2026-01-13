package com.zqksk.mail.config;

import com.zqksk.mail.model.Mail;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Getter
@Component
public class MailQueueHolder {
    private final Queue<Mail> sharedMailQueue = new ConcurrentLinkedQueue<>();
}
