package com.synpulse8.ebank.Services;

import com.synpulse8.ebank.DTO.AccountCreationDTO;
import com.synpulse8.ebank.Models.Account;

import java.util.List;

public interface AccountService {
    Account getAccountById(Long id);
    List<Account> getAccountForUser(Long userId);
    void accountCreation(AccountCreationDTO request);
    AccountCreationDTO getCreationStatus(String iban);
    Account getAccountByIban(String iban);
    void updateAccountCreationStatus(AccountCreationDTO accountCreationDTO);
}
