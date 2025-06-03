package com.kaarelel.accountservice.service;

import com.kaarelel.accountservice.entity.Account;
import com.kaarelel.accountservice.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private Account sampleAccount;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleAccount = new Account();
        sampleAccount.setId(UUID.randomUUID());
        sampleAccount.setUsername("testuser");
        sampleAccount.setPhone("+3725555555");
    }

    @Test
    void create_shouldSaveAndReturnAccount() {
        when(accountRepository.save(any(Account.class))).thenReturn(sampleAccount);

        Account result = accountService.create(sampleAccount, UUID.randomUUID());

        assertEquals("testuser", result.getUsername());
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void getById_shouldReturnAccount() {
        UUID id = sampleAccount.getId();
        when(accountRepository.findById(id)).thenReturn(Optional.of(sampleAccount));

        Optional<Account> found = accountService.getById(id);

        assertTrue(found.isPresent());
        assertEquals("testuser", found.get().getUsername());
    }

    @Test
    void update_shouldUpdateUsernameAndPhone() {
        UUID id = sampleAccount.getId();
        Account update = new Account();
        update.setUsername("newname");
        update.setPhone("59878956");

        when(accountRepository.findById(id)).thenReturn(Optional.of(sampleAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(sampleAccount);

        Account updated = accountService.update(id, update);

        assertEquals("newname", updated.getUsername());
        assertEquals("59878956", updated.getPhone());
    }

    @Test
    void delete_shouldDeleteAccount() {
        UUID id = sampleAccount.getId();
        when(accountRepository.findById(id)).thenReturn(Optional.of(sampleAccount));

        accountService.delete(id);

        verify(accountRepository).delete(sampleAccount);
    }

    @Test
    void update_shouldThrowIfUsernameInvalid() {
        UUID id = sampleAccount.getId();
        Account update = new Account();
        update.setUsername("");

        when(accountRepository.findById(id)).thenReturn(Optional.of(sampleAccount));

        assertThrows(IllegalArgumentException.class, () -> accountService.update(id, update));
    }
}
