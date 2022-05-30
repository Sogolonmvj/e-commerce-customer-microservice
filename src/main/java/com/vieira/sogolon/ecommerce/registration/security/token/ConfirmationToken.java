package com.vieira.sogolon.ecommerce.registration.security.token;

import com.vieira.sogolon.ecommerce.registration.model.UserCustomer;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class ConfirmationToken {

    @SequenceGenerator(name = "confirmation_token_sequence",
            sequenceName = "confirmation_token_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "confirmation_token_sequence"
    )
    private Long id;
    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn (
            nullable = false,
            name = "customer_id"
    )
    private UserCustomer userCustomer;

    public ConfirmationToken(String token, LocalDateTime createdAt, LocalDateTime expiresAt, UserCustomer userCustomer) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.userCustomer = userCustomer;
    }
}
