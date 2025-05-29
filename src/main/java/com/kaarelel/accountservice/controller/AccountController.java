package com.kaarelel.accountservice.controller;

import com.kaarelel.accountservice.entity.Account;
import com.kaarelel.accountservice.service.AccountService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    @ApiResponse(description = "Create a new account")
    public ResponseEntity<?> createAccount(@Valid @RequestBody Account account) {
        Account existingAccount = accountService.findByUsername(account.getUsername());

        if (existingAccount != null ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Account with that username already exists");
        }

        Account created = accountService.create(account, UUID.randomUUID());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @ApiResponse(description = "Get account information via ID")
    public ResponseEntity<?> accountInformationViaId(@PathVariable UUID id) {
        Optional<Account> account = accountService.getById(id);

        if (account.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
        }

        return ResponseEntity.ok(account.get());
    }

    @PutMapping("/{id}")
    @ApiResponse(description = "Update account information")
    public ResponseEntity<?> update(@PathVariable UUID id, @Valid @RequestBody Account account) {
        Account updated = accountService.update(id, account);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(description = "Delete account")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        try {
            accountService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found with ID: " + id);
        }
    }
}
