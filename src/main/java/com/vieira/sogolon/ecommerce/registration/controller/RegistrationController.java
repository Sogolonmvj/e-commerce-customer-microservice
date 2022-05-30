package com.vieira.sogolon.ecommerce.registration.controller;

import com.vieira.sogolon.ecommerce.registration.model.RegistrationRequest;
import com.vieira.sogolon.ecommerce.registration.services.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/user")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/create")
    public String register(@RequestBody RegistrationRequest request) {
        return registrationService.register(request);
    }

    @GetMapping(path="/create/confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }

}
