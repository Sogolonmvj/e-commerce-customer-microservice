package com.vieira.sogolon.ecommerce.registration.sender;

public interface EmailSender {
    void send(String to, String email);
}
