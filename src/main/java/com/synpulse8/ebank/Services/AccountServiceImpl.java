package com.synpulse8.ebank.Services;

import com.synpulse8.ebank.DTO.AccountRequest;
import com.synpulse8.ebank.Exceptions.BankAccountNonExistException;
import com.synpulse8.ebank.Models.Account;
import com.synpulse8.ebank.Repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService{

    private final AccountRepository accountRepository;

    @Override
    public Account createAccount(AccountRequest request) {
        Account acc = Account.builder()
                .currency(request.getCurrency())
                .credit_amt(BigDecimal.valueOf(1))
                .debit_amt(BigDecimal.valueOf(0))
                .build();
        return accountRepository.save(acc);
    }

    @Override
    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElseThrow(
                () -> new BankAccountNonExistException("Account not found"));
    }
}
