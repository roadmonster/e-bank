package com.synpulse8.ebank.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.synpulse8.ebank.DTO.AccountCreation;
import com.synpulse8.ebank.Exceptions.BankAccountNonExistException;
import com.synpulse8.ebank.Services.Account.AccountService;
import com.synpulse8.ebank.Utilities.IBANGenerator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping
    public ResponseEntity<String> createAccount(@RequestBody AccountCreation accountCreation) {
        String iban = IBANGenerator.generate(accountCreation.getCode());
        accountCreation.setIban(iban);
        accountService.accountCreation(accountCreation);
        return ResponseEntity.accepted().body("Account creation accepted and here is your account number: " + iban);
    }

    @GetMapping("/status")
    public ResponseEntity<String> getAccountStatus(@RequestParam String iban) {
        AccountCreation dto = accountService.getCreationStatus(iban);
        if (dto == null) {
            return ResponseEntity.badRequest().body("iban non existent");
        }
        return ResponseEntity.ok().body("status; " + dto.getStatus());

    }

    @GetMapping
    public ResponseEntity<String> getAccountByIban(@RequestParam String iban) {
        try {
            return ResponseEntity.ok().body(objectMapper.writeValueAsString(accountService.getAccountByIban(iban)));
        } catch (BankAccountNonExistException e) {
            return ResponseEntity.badRequest().body("Invalid iban");
        } catch (JsonProcessingException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
