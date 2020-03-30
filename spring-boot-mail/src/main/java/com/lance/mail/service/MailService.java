package com.lance.mail.service;

/**
 * @Description:
 * @author: zhaotian
 * @date: 2020/3/30
 */
public interface MailService {
    void sendSimpleMail(String to, String subject, String content);
}
