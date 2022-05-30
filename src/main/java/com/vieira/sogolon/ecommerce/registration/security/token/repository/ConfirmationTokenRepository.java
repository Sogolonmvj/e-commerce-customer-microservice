package com.vieira.sogolon.ecommerce.registration.security.token.repository;

import com.vieira.sogolon.ecommerce.registration.security.token.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {

    Optional<ConfirmationToken> findByToken(String token); // query here

    @Transactional
    @Modifying
    @Query("UPDATE ConfirmationToken s " +
            "SET s.confirmedAt = ?2 " +
            "WHERE s.token = ?1"
    )
    int updateConfirmedAt(String token, LocalDateTime confirmedAt);
}
