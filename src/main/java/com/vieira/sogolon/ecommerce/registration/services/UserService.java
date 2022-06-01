package com.vieira.sogolon.ecommerce.registration.services;

import com.vieira.sogolon.ecommerce.registration.client.AddressClient;
import com.vieira.sogolon.ecommerce.registration.dto.UserDTO;
import com.vieira.sogolon.ecommerce.registration.enums.UserRole;
import com.vieira.sogolon.ecommerce.registration.model.Address;
import com.vieira.sogolon.ecommerce.registration.model.RegistrationRequest;
import com.vieira.sogolon.ecommerce.registration.model.UserCustomer;
import com.vieira.sogolon.ecommerce.registration.repository.UserRepository;
import com.vieira.sogolon.ecommerce.registration.security.token.ConfirmationToken;
import com.vieira.sogolon.ecommerce.registration.security.token.services.ConfirmationTokenService;
import com.vieira.sogolon.ecommerce.registration.sender.EmailSender;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
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
    private final AddressClient addressClient;
    private final EmailSender emailSender;
    private final Environment env;

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

            if (!userCustomer.getEnabled()) {

                String newToken = UUID.randomUUID().toString();

                String link = env.getProperty("sogolon.token-url") + newToken;
                emailSender.send(
                        userCustomer.getEmail(),
                        buildEmail(userCustomer.getFirstName(), link));

                ConfirmationToken confirmationToken = new ConfirmationToken(
                        newToken,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusMinutes(1),
                        userRepository.findByEmail(userCustomer.getEmail()).get()
                );

                confirmationTokenService.saveConfirmationToken(confirmationToken);

                throw new IllegalStateException("Email não ativado! Por favor, ative seu email para utilizar nossos serviços!");
            }

            throw new IllegalStateException("Usuário já registrado!");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(userCustomer.getPassword());

        userCustomer.setPassword(encodedPassword);

        Address address = addressClient.getAddressByCEP(userCustomer.getCep());

        if (address != null) {
            userCustomer.setCity(address.getLocalidade());
            userCustomer.setStreet(address.getLogradouro());
            userCustomer.setDistrict(address.getBairro());
        }

        userRepository.save(userCustomer);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(1),
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

            if (user.getEnabled()) {
                userDTO.setId(user.getId());
                userDTO.setFirstName(user.getFirstName());
                userDTO.setLastName(user.getLastName());
                userDTO.setEmail(user.getEmail());
                userDTO.setCep(user.getCep());
                userDTO.setCity(user.getCity());
                userDTO.setStreet(user.getStreet());
                userDTO.setDistrict(user.getDistrict());

                userDTOS.add(userDTO);
            }

        }

        return userDTOS;
    }

    public Optional<UserDTO> getUserCustomer(String email) {
        Optional<UserCustomer> user = userRepository.findByEmail(email);

        Optional<UserDTO> userDTO = Optional.of(new UserDTO());

        if (user.isPresent() && user.get().getEnabled()) {
            userDTO.get().setId(user.get().getId());
            userDTO.get().setFirstName(user.get().getFirstName());
            userDTO.get().setLastName(user.get().getLastName());
            userDTO.get().setEmail(user.get().getEmail());
            userDTO.get().setCep(user.get().getCep());
            userDTO.get().setCity(user.get().getCity());
            userDTO.get().setStreet(user.get().getStreet());
            userDTO.get().setDistrict(user.get().getDistrict());
        }

        return userDTO;
    }

    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#DFFF00;text-decoration:none;vertical-align:top;display:inline-block\">Confirme seu email!</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#DFFF00\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#008080\">Olá " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#008080\"> Obrigado(a) por se registrar. Por favor, ative sua conta clicando no link abaixo: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#008080\"> <a style=\"color:#008000\" href=\"" + link + "\">Confirme agora.</a> </p></blockquote>\n  <p style=\"color:#008080\">Link expira em 10 minutos.</p> <p style=\"color:#008080\">Até logo!</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }

}
