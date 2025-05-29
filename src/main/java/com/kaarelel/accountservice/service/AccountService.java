package com.kaarelel.accountservice.service;

import com.kaarelel.accountservice.entity.Account;
import com.kaarelel.accountservice.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static org.ietf.jgss.GSSException.FAILURE;
import static org.springframework.scheduling.config.TaskExecutionOutcome.Status.SUCCESS;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private static final Logger log = LoggerFactory.getLogger(AccountService.class);

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public Optional<Account> getById(UUID id) {
        return accountRepository.findById(id);
    }

    public Account create(Account account, UUID requestId) {
        if (account.getUsername() == null || account.getUsername().isBlank()) {
            log.warn(FAILURE + "Username cannot be empty. Request ID is {}", requestId);
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        Account created = accountRepository.save(account);
        log.info(SUCCESS + "Account with ID {} and username {} is created at {}. Request ID is {}",
                created.getId(), created.getUsername(), created.getCreatedAt(), requestId);
        return created;
    }


    public Account update(UUID id, Account update) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + id));

        if (update.getUsername() == null || update.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        if (!update.getUsername().equals(account.getUsername())) {
            account.setUsername(update.getUsername());
        }

        if (update.getPhone() != null && !update.getPhone().isBlank()) {
            String phoneRegex = "^(\\+\\d{7,15}|\\d{7,9})$";
            if (!update.getPhone().matches(phoneRegex)) {
                throw new IllegalArgumentException("Phone number format is invalid");
            }

            if (!update.getPhone().equals(account.getPhone())) {
                account.setPhone(update.getPhone());
            }
        }

        Account updated = accountRepository.save(account);
        log.info(SUCCESS + "Account {} with ID {} is updated at {}", updated.getUsername(), updated.getId(), updated.getUpdatedAt());
        return updated;
    }

    public void delete(UUID id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + id));

        accountRepository.delete(account);
        log.info(SUCCESS + "Account with ID {} is deleted successfully", id);
    }
}
