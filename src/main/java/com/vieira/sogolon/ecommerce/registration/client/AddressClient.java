package com.vieira.sogolon.ecommerce.registration.client;

import com.vieira.sogolon.ecommerce.registration.model.Address;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="address")
public interface AddressClient {

    @GetMapping(value="")
    Address getAddressByCep(@PathVariable String cep);

}
