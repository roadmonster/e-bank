package com.synpulse8.ebank.Services;

import com.synpulse8.ebank.DTO.AccountRequest;
import com.synpulse8.ebank.Exceptions.BankAccountNonExistException;
import com.synpulse8.ebank.Exceptions.UserNotFoundException;
import com.synpulse8.ebank.Models.Account;
import com.synpulse8.ebank.Models.User;
import com.synpulse8.ebank.Repository.AccountRepository;
import com.synpulse8.ebank.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService{

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Override
    public Account createAccount(AccountRequest request) {
        User user = userRepository.findById(request.getUserId()).orElseThrow(
                () -> new UserNotFoundException("Not matching result for given userId")
        );
        Account acc = Account.builder()
                .currency(request.getCurrency())
                .credit_amt(BigDecimal.valueOf(1))
                .debit_amt(BigDecimal.valueOf(0))
                .user(user)
                .build();
        return accountRepository.save(acc);
    }

    @Override
    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElseThrow(
                () -> new BankAccountNonExistException("Account not found"));
    }

    @Override
    public List<Account> getAccountForUser(Long userId) {
        return accountRepository.findAllAccountsByUserId(userId);
    }
}
