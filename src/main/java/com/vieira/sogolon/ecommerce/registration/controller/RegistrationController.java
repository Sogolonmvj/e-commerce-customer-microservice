package com.vieira.sogolon.ecommerce.registration.controller;

import com.vieira.sogolon.ecommerce.registration.model.RegistrationRequest;
import com.vieira.sogolon.ecommerce.registration.services.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/user")
@AllArgsConstructor
public class RegistrationController {

    private RegistrationService registrationService;

    @PostMapping("/create")
    public String register(@RequestBody RegistrationRequest request) {
        return registrationService.register(request);
    }
}
