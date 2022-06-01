package com.vieira.sogolon.ecommerce.registration.controller;

import com.vieira.sogolon.ecommerce.registration.dto.UserDTO;
import com.vieira.sogolon.ecommerce.registration.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> fetchAllCustomers() {
        List<UserDTO> users = userService.getAllCustomers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<?> fetchCustomer(@PathVariable("email") String email) {
        Optional<UserDTO> customerEmail = userService.getUserCustomer(email);

        if (customerEmail.isPresent() && customerEmail.get().getId() != null) {
            return ResponseEntity.ok(customerEmail);
        }

        return ResponseEntity.notFound().build();
    }

}
