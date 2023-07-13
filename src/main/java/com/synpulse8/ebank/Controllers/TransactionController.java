package com.synpulse8.ebank.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.synpulse8.ebank.DTO.PersonalTransactionRequest;
import com.synpulse8.ebank.DTO.SendReceiveTransactionRequest;
import com.synpulse8.ebank.DTO.TransactionResponse;
import com.synpulse8.ebank.Exceptions.BankAccountNonExistException;
import com.synpulse8.ebank.Exceptions.BankTransactionNotFoundException;
import com.synpulse8.ebank.Models.Transaction;
import com.synpulse8.ebank.Services.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
@AllArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/personal")
    public ResponseEntity<String> createPersonalTransaction(@RequestBody PersonalTransactionRequest request) {
        try{
            Transaction t = transactionService.createPersonalTransaction(request);
            return new ResponseEntity<>(objectMapper.writeValueAsString(t), HttpStatus.CREATED);
        } catch (BankAccountNonExistException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/sendReceive")
    public ResponseEntity<String> createSendReceiveTransaction(@RequestBody SendReceiveTransactionRequest request) {
        try {
            TransactionResponse response = transactionService.createSendReceiveTransaction(request);
            return new ResponseEntity<>(objectMapper.writeValueAsString(response), HttpStatus.CREATED);
        } catch (BankAccountNonExistException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getTransactionById (@PathVariable Long id){
        try {
            Transaction t = transactionService.getTransactionById(id);
            return new ResponseEntity<>(objectMapper.writeValueAsString(t),
                    HttpStatus.OK);
        } catch (BankTransactionNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}