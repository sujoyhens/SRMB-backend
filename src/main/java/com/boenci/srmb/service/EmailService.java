package com.boenci.srmb.service;

import com.boenci.srmb.model.Mail;

public interface EmailService {
    void send(Mail mail);
}
