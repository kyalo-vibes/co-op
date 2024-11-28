package com.kyalo.banksimple.account;

import com.kyalo.banksimple.transaction.TransactionRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAccounts() { return ResponseEntity.ok(accountService.getAccounts()); }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable Long id) { return ResponseEntity.ok(accountService.getAccountById(id)); }

    @PostMapping
    public ResponseEntity<AccountResponseDTO> createAccount(@Valid @RequestBody AccountRequestDTO accountRequestDTO) { return ResponseEntity.ok(accountService.createAccount(accountRequestDTO)); }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<AccountResponseDTO> deposit(@PathVariable Long id, @Valid @RequestBody TransactionRequestDTO transactionRequestDTO) { return ResponseEntity.ok(accountService.deposit(id, transactionRequestDTO.amount())); }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<AccountResponseDTO> withdraw(@PathVariable Long id, @Valid @RequestBody TransactionRequestDTO transactionRequestDTO) { return ResponseEntity.ok(accountService.withdraw(id, transactionRequestDTO.amount())); }

    // Transfer endpoint
    @PostMapping("/{sourceAccountId}/transfer/{targetAccountId}")
    public ResponseEntity<AccountResponseDTO> transfer(
            @PathVariable Long sourceAccountId,
            @PathVariable Long targetAccountId,
            @Valid @RequestBody TransactionRequestDTO transactionRequestDTO) {
        AccountResponseDTO accountResponseDTO = accountService.transfer(sourceAccountId, targetAccountId, transactionRequestDTO.amount());
        return ResponseEntity.ok(accountResponseDTO);
    }
}
