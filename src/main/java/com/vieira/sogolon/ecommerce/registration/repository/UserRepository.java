package com.vieira.sogolon.ecommerce.registration.repository;

import com.vieira.sogolon.ecommerce.registration.model.UserCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<UserCustomer, Long> {
    Optional<UserCustomer> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE UserCustomer c " +
            "SET c.enabled = TRUE WHERE c.email = ?1")
    int enableUser(String email);
}
