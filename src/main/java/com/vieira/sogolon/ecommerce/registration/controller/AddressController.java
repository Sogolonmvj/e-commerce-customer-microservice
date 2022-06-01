package com.vieira.sogolon.ecommerce.registration.controller;

import com.vieira.sogolon.ecommerce.registration.client.AddressClient;
import com.vieira.sogolon.ecommerce.registration.model.Address;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/get/address")
@AllArgsConstructor
public class AddressController {

    private final AddressClient addressClient;

    @GetMapping("/{cep}")
    public Address getAddressByCEP(@PathVariable String cep) {
        return addressClient.getAddressByCEP(cep);
    }
}
