package com.synpulse8.ebank.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.synpulse8.ebank.DTO.AccountCreationDTO;
import com.synpulse8.ebank.Exceptions.BankAccountNonExistException;
import com.synpulse8.ebank.Models.Account;
import com.synpulse8.ebank.Services.AccountService;
import com.synpulse8.ebank.Utilities.IBANGenerator;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping
    public ResponseEntity<String> createAccount(@RequestBody AccountCreationDTO accountCreationDTO) {
        System.out.println("I am in the controller and here is the account dto " + accountCreationDTO.toString());
        String iban = IBANGenerator.generate(accountCreationDTO.getCode());
        accountCreationDTO.setIban(iban);
        accountService.accountCreation(accountCreationDTO);
        return ResponseEntity.accepted().body("Account creation accepted and here is your account number: " + iban);
    }

    @GetMapping("/status")
    public ResponseEntity<String> getAccountById(@RequestParam String iban) {
        AccountCreationDTO dto = accountService.getCreationStatus(iban);
        if (dto == null) {
            return ResponseEntity.badRequest().body("iban non existent");
        }
        return ResponseEntity.ok().body("status; " + dto.getStatus());

    }

    private String getAccountJson(Account acc) throws JsonProcessingException {
        String accountJson = objectMapper.writeValueAsString(acc);
        return accountJson;
    }

}
