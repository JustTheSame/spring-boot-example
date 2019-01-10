package com.lance.mail.service;

public interface MailService {
    public void sendSimpleMail(String to, String subject, String content);
}
