package com.vieira.sogolon.ecommerce.registration.dto;

import com.vieira.sogolon.ecommerce.registration.enums.UserRole;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class UserCustomerDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String cep;
    private String city;
    private String street;
    private String district;
    private String cpf;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;
    private Boolean locked = false;
    private Boolean enabled = false;

    

}
