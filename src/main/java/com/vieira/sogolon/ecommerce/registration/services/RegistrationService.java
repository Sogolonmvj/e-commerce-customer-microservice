package com.vieira.sogolon.ecommerce.registration.services;

import com.vieira.sogolon.ecommerce.registration.enums.UserRole;
import com.vieira.sogolon.ecommerce.registration.model.RegistrationRequest;
import com.vieira.sogolon.ecommerce.registration.model.User;
import com.vieira.sogolon.ecommerce.registration.validators.EmailValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final UserService userService;
    private final EmailValidator emailValidator;

    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.
                test(request.getEmail());
        if (!isValidEmail) {
            throw new IllegalStateException("Email inválido!");
        }
        return userService.signUpUser(
                new User(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getCep(),
                        request.getCpf(),
                        request.getPassword(),
                        UserRole.USER
                        )
        );
    }
}
