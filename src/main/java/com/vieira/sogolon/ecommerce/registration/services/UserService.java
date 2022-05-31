package com.vieira.sogolon.ecommerce.registration.services;

import com.vieira.sogolon.ecommerce.registration.dto.UserDTO;
import com.vieira.sogolon.ecommerce.registration.model.UserCustomer;
import com.vieira.sogolon.ecommerce.registration.repository.UserRepository;
import com.vieira.sogolon.ecommerce.registration.security.token.ConfirmationToken;
import com.vieira.sogolon.ecommerce.registration.security.token.services.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MESSAGE = "Usuário %s não encontrado.";
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException(
                        String.format(USER_NOT_FOUND_MESSAGE, email)));
    }

    public String signUpUser(UserCustomer userCustomer) {
        boolean userExists = userRepository.findByEmail(userCustomer.getEmail())
                .isPresent();
        if (userExists) {


            // Complete

            throw new IllegalStateException("Usuário já registrado!");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(userCustomer.getPassword());

        userCustomer.setPassword(encodedPassword);

        userRepository.save(userCustomer);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(10),
                userCustomer
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return token;
    }

    public int enableUser(String email) {
        return userRepository.enableUser(email);
    }

    public List<UserDTO> getAllCustomers() {
        List<UserCustomer> users = userRepository.findAll();

        List<UserDTO> userDTOS = new ArrayList<>();

        for (UserCustomer user: users) {

            UserDTO userDTO = new UserDTO();

            userDTO.setId(user.getId());
            userDTO.setFirstName(user.getFirstName());
            userDTO.setLastName(user.getLastName());
            userDTO.setEmail(user.getEmail());
            userDTO.setCep(user.getCep());

            userDTOS.add(userDTO);

        }

        return userDTOS;
    }

    public Optional<UserDTO> getUserCustomer(String email) {
        Optional<UserCustomer> user = userRepository.findByEmail(email);

        Optional<UserDTO> userDTO = Optional.of(new UserDTO());
        
        if (user.isPresent()) {
            userDTO.get().setId(user.get().getId());
            userDTO.get().setFirstName(user.get().getFirstName());
            userDTO.get().setLastName(user.get().getLastName());
            userDTO.get().setEmail(user.get().getEmail());
            userDTO.get().setCep(user.get().getCep());
        }

        return userDTO;
    }

}
