package com.example.notificationservice.service;

public interface NotificationService {
    void send(String toEmail, String subject, String content);
}
