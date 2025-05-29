package com.kaarelel.accountservice.repository;

import com.kaarelel.accountservice.entity.Account;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    Account findByUsername(String username);
}
