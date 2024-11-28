package com.kyalo.banksimple.account;

import com.kyalo.banksimple.transaction.TransactionRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.logging.Logger;


@RestController
@RequestMapping("api/v1/accounts")
public class AccountController {
    private static final Logger logger = Logger.getLogger(AccountController.class.getName());
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAccounts() {
        logger.info("Fetching all accounts");
        return ResponseEntity.ok(accountService.getAccounts()); }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable Long id) {
        logger.info("Fetching account with id: " + id);
        return ResponseEntity.ok(accountService.getAccountById(id)); }

    @PostMapping
    public ResponseEntity<AccountResponseDTO> createAccount(@Valid @RequestBody AccountRequestDTO accountRequestDTO) {
        logger.info("Creating new account for: " + accountRequestDTO.accountHolder());
        return ResponseEntity.ok(accountService.createAccount(accountRequestDTO)); }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<AccountResponseDTO> deposit(@PathVariable Long id, @Valid @RequestBody TransactionRequestDTO transactionRequestDTO) {
        logger.info("Depositing amount: " + transactionRequestDTO.amount() + " to account id: " + id);
        return ResponseEntity.ok(accountService.deposit(id, transactionRequestDTO.amount())); }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<AccountResponseDTO> withdraw(@PathVariable Long id, @Valid @RequestBody TransactionRequestDTO transactionRequestDTO) {
        logger.info("Withdrawing amount: " + transactionRequestDTO.amount() + " from account id: " + id);
        return ResponseEntity.ok(accountService.withdraw(id, transactionRequestDTO.amount())); }

    // Transfer endpoint
    @PostMapping("/{sourceAccountId}/transfer/{targetAccountId}")
    public ResponseEntity<AccountResponseDTO> transfer(
            @PathVariable Long sourceAccountId,
            @PathVariable Long targetAccountId,
            @Valid @RequestBody TransactionRequestDTO transactionRequestDTO) {
        logger.info("Transferring amount: " + transactionRequestDTO.amount() + " from account id: " + sourceAccountId + " to account id: " + targetAccountId);
        AccountResponseDTO accountResponseDTO = accountService.transfer(sourceAccountId, targetAccountId, transactionRequestDTO.amount());
        return ResponseEntity.ok(accountResponseDTO);
    }
}
