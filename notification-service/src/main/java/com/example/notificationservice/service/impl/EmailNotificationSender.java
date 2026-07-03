package com.example.notificationservice.service.impl;

import com.example.notificationservice.exception.ApplicationException;
import com.example.notificationservice.exception.ErrorCode;
import com.example.notificationservice.service.NotificationService;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailNotificationSender implements NotificationService {

    JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    @NonFinal
    String from;

    @Override
    public void send(String toEmail, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(from);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(content, true); // true = HTML

            mailSender.send(message);
            log.info("Email sent to {}", toEmail);
        } catch (Exception e) {
            log.error("Send email failed to {}: {}", toEmail, e.getMessage(), e);
            throw new ApplicationException(ErrorCode.SEND_EMAIL_FAILED);
        }
    }
}
