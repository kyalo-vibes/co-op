package com.kyalo.banksimple.account;

import com.kyalo.banksimple.exception.BadRequestException;
import com.kyalo.banksimple.exception.InsufficientBalanceException;
import com.kyalo.banksimple.exception.ResourceNotFoundException;
import com.kyalo.banksimple.transaction.Transaction;
import com.kyalo.banksimple.transaction.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

import jakarta.validation.Valid;


@Service
public class AccountService {
    private static final Logger logger = Logger.getLogger(AccountService.class.getName());
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public List<Account> getAccounts() {
        List<Account> accounts = accountRepository.findAll();
        if (accounts.isEmpty()) {
            logger.warning("No accounts found");
            throw new ResourceNotFoundException("No accounts found");
        }
        logger.fine("Accounts fetched successfully: " + accounts.size());
        return accounts;
    }

    @Transactional
    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> {
            logger.severe("Account not found with id: " + id);
            return new ResourceNotFoundException("Account not found with id: " + id);
        });

    }

    @Transactional
    public AccountResponseDTO deposit(Long id, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            logger.warning("Deposit amount must be positive: " + amount);
            throw new BadRequestException("Deposit amount must be positive");
        }

        // Find the account
        Account account = accountRepository.findById(id).orElseThrow(() -> {
            logger.severe("Account not found with id: " + id);
            return new ResourceNotFoundException("Account not found with id: " + id);
        });


        // Update account balance
        account.setBalance(account.getBalance().add(amount));

        // Save the updated account
        Account updatedAccount = accountRepository.save(account);

        // Log the deposit transaction
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setTransactionType("DEPOSIT");
        transaction.setAmount(amount);
        transactionRepository.save(transaction);

        // Return updated account response
        logger.fine("Deposit successful. Updated balance: " + updatedAccount.getBalance());
        return new AccountResponseDTO(updatedAccount.getId(), updatedAccount.getAccountHolder(), updatedAccount.getBalance());
    }

    @Transactional
    public AccountResponseDTO withdraw(Long id, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            logger.warning("Withdrawal amount must be positive: " + amount);
            throw new BadRequestException("Withdrawal amount must be positive");
        }

        Account account = accountRepository.findById(id).orElseThrow(() -> {
            logger.severe("Account not found with id: " + id);
            return new ResourceNotFoundException("Account not found with id: " + id);
        });


        if (account.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
            logger.warning("Insufficient balance for withdrawal. Current balance: " + account.getBalance() + ", requested amount: " + amount);
            throw new InsufficientBalanceException("Insufficient balance for withdrawal");
        }

        account.setBalance(account.getBalance().subtract(amount));

        Account updatedAccount = accountRepository.save(account);
        return new AccountResponseDTO(updatedAccount.getId(), updatedAccount.getAccountHolder(), updatedAccount.getBalance());
    }

    @Transactional
    public AccountResponseDTO createAccount(@Valid AccountRequestDTO accountRequestDTO) {
        if (accountRequestDTO.initialBalance().compareTo(BigDecimal.ZERO) < 0) {
            logger.warning("Initial balance must be non-negative: " + accountRequestDTO.initialBalance());
            throw new BadRequestException("Initial balance must be non-negative");
        }

        Account account = new Account();
        account.setAccountHolder(accountRequestDTO.accountHolder());
        account.setBalance(accountRequestDTO.initialBalance());

        Account savedAccount = accountRepository.save(account);
        logger.fine("Account created successfully with ID: " + savedAccount.getId());
        return new AccountResponseDTO(savedAccount.getId(), savedAccount.getAccountHolder(), savedAccount.getBalance());
    }

    @Transactional
    public AccountResponseDTO transfer(Long sourceAccountId, Long targetAccountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            logger.warning("Transfer amount must be positive: " + amount);
            throw new BadRequestException("Transfer amount must be positive");
        }

        // Find the source account
        Account sourceAccount = accountRepository.findById(sourceAccountId).orElseThrow(() -> {
            logger.severe("Source account not found with id: " + sourceAccountId);
            return new ResourceNotFoundException("Source account not found with id: " + sourceAccountId);
        });


        // Find the target account
        Account targetAccount = accountRepository.findById(targetAccountId).orElseThrow(() -> {
            logger.severe("Target account not found with id: " + targetAccountId);
            return new ResourceNotFoundException("Target account not found with id: " + targetAccountId);
        });


        // Ensure the source account has sufficient balance
        if (sourceAccount.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
            logger.warning("Insufficient balance for transfer. Source account balance: " + sourceAccount.getBalance() + ", requested amount: " + amount);
            throw new InsufficientBalanceException("Insufficient balance for the transfer");
        }

        // Deduct the amount from the source account
        sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
        accountRepository.save(sourceAccount); // Save the updated source account

        // Add the amount to the target account
        targetAccount.setBalance(targetAccount.getBalance().add(amount));
        accountRepository.save(targetAccount); // Save the updated target account

        // Log the withdrawal transaction from the source account
        Transaction withdrawalTransaction = new Transaction();
        withdrawalTransaction.setAccount(sourceAccount);
        withdrawalTransaction.setTransactionType("WITHDRAWAL");
        withdrawalTransaction.setAmount(amount);
        transactionRepository.save(withdrawalTransaction);

        // Log the deposit transaction to the target account
        Transaction depositTransaction = new Transaction();
        depositTransaction.setAccount(targetAccount);
        depositTransaction.setTransactionType("TRANSFER");
        depositTransaction.setAmount(amount);
        transactionRepository.save(depositTransaction);

        // Return updated account response for the source account (or both if needed)
        logger.fine("Transfer successful. Source account balance: " + sourceAccount.getBalance() + ", target account balance: " + targetAccount.getBalance());
        return new AccountResponseDTO(sourceAccount.getId(), sourceAccount.getAccountHolder(), sourceAccount.getBalance());
    }

}
