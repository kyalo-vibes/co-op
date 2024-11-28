package com.kyalo.banksimple.account;

import com.kyalo.banksimple.exception.ResourceNotFoundException;
import com.kyalo.banksimple.transaction.Transaction;
import com.kyalo.banksimple.transaction.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Enable Mockito annotations
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private AccountService accountService;

    private Account account;

    @BeforeEach
    public void setUp() {
        // Initialize test data
        account = new Account();
        account.setId(1L);
        account.setAccountHolder("John Doe");
        account.setBalance(new BigDecimal("1000.00"));
    }

    @Test
    public void testGetAccountById_Success() {
        // Mock the repository call
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        Account result = accountService.getAccountById(1L);

        assert result != null;
        assert result.getId().equals(1L);
        assert result.getAccountHolder().equals("John Doe");
    }

    @Test
    public void testGetAccountById_NotFound() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        // Expecting exception
        try {
            accountService.getAccountById(1L);
        } catch (ResourceNotFoundException ex) {
            assert ex.getMessage().equals("Account not found with id: 1");
        }
    }

    @Test
    public void testDeposit_Success() {
        BigDecimal depositAmount = new BigDecimal("500.00");

        // Mock repository
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        AccountResponseDTO response = accountService.deposit(1L, depositAmount);

        assert response != null;
        assert response.balance().equals(new BigDecimal("1500.00"));
    }


    @Test
    public void testWithdraw_Success() {
        BigDecimal withdrawAmount = new BigDecimal("200.00");

        // Mock repository
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        AccountResponseDTO response = accountService.withdraw(1L, withdrawAmount);

        assert response != null;
        assert response.balance().equals(new BigDecimal("800.00"));
    }


    @Test
    public void testTransfer_Success() {
        BigDecimal transferAmount = new BigDecimal("300.00");

        Account targetAccount = new Account();
        targetAccount.setId(2L);
        targetAccount.setAccountHolder("Target Account");
        targetAccount.setBalance(new BigDecimal("200.00"));

        // Mock repositories
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(targetAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());

        AccountResponseDTO response = accountService.transfer(1L, 2L, transferAmount);

        assert response != null;
        assert response.balance().equals(new BigDecimal("700.00"));
    }
}
