package com.kyalo.banksimple.account;

import com.kyalo.banksimple.exception.BadRequestException;
import com.kyalo.banksimple.exception.InsufficientBalanceException;
import com.kyalo.banksimple.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import jakarta.validation.Valid;


@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public List<Account> getAccounts() {
        List<Account> accounts = accountRepository.findAll();
        if (accounts.isEmpty()) {
            throw new ResourceNotFoundException("No accounts found");
        }
        return accounts;
    }

    @Transactional
    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Account not found with id: " + id));
    }

    @Transactional
    public AccountResponseDTO deposit(Long id, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Deposit amount must be positive");
        }

        Account account = accountRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Account not found with id: " + id));

        account.setBalance(account.getBalance().add(amount));

        Account updatedAccount = accountRepository.save(account);
        return new AccountResponseDTO(updatedAccount.getId(), updatedAccount.getAccountHolder(), updatedAccount.getBalance());
    }

    @Transactional
    public AccountResponseDTO withdraw(Long id, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Withdrawal amount must be positive");
        }

        Account account = accountRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Account not found with id: " + id));

        if (account.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientBalanceException("Insufficient balance for withdrawal");
        }

        account.setBalance(account.getBalance().subtract(amount));

        Account updatedAccount = accountRepository.save(account);
        return new AccountResponseDTO(updatedAccount.getId(), updatedAccount.getAccountHolder(), updatedAccount.getBalance());
    }

    @Transactional
    public AccountResponseDTO createAccount(@Valid AccountRequestDTO accountRequestDTO) {
        if (accountRequestDTO.initialBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Initial balance must be non-negative");
        }

        Account account = new Account();
        account.setAccountHolder(accountRequestDTO.accountHolder());
        account.setBalance(accountRequestDTO.initialBalance());

        Account savedAccount = accountRepository.save(account);
        return new AccountResponseDTO(savedAccount.getId(), savedAccount.getAccountHolder(), savedAccount.getBalance());
    }
}
