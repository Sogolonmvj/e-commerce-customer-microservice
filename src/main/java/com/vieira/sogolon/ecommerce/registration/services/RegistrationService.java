package com.vieira.sogolon.ecommerce.registration.services;

import com.vieira.sogolon.ecommerce.registration.model.RegistrationRequest;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    public String register(RegistrationRequest request) {
        return "works";
    }
}
