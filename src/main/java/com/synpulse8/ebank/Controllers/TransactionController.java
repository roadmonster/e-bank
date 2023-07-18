package com.synpulse8.ebank.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.synpulse8.ebank.DTO.*;
import com.synpulse8.ebank.Exceptions.BankTransactionNotFoundException;
import com.synpulse8.ebank.Models.Transaction;
import com.synpulse8.ebank.Services.TransactionService;
import com.synpulse8.ebank.Utilities.Timestamper;
import com.synpulse8.ebank.Utilities.UUIDGenerator;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transaction")
@AllArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final ObjectMapper objectMapper = new ObjectMapper();

//    @PostMapping("/personal")
//    public ResponseEntity<String> createPersonalTransaction(@RequestBody PersonalTransactionRequest request) {
//        try{
//            Transaction t = transactionService.createPersonalTransaction(request);
//            return new ResponseEntity<>(objectMapper.writeValueAsString(t), HttpStatus.CREATED);
//        } catch (BankAccountNonExistException e) {
//            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @PostMapping("/sendReceive")
//    public ResponseEntity<String> createSendReceiveTransaction(@RequestBody SendReceiveTransactionRequest request) {
//        try {
//            System.out.println(request.getTransactionTime());
//            TransactionResponse response = transactionService.createSendReceiveTransaction(request);
//            return new ResponseEntity<>(objectMapper.writeValueAsString(response), HttpStatus.CREATED);
//        } catch (BankAccountNonExistException e) {
//            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getTransactionById (@PathVariable UUID id){
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

    @GetMapping("/{year}/{month}/{day}")
    public ResponseEntity<String> getTransactionForCertainDay(@PathVariable int year,
                                                              @PathVariable int month,
                                                              @PathVariable int day) throws JsonProcessingException {
        List<Transaction> transactions = transactionService.getTransactionByDate(year, month, day);
        return new ResponseEntity<>(objectMapper.writeValueAsString(transactions), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<String> getAllTransactions() throws JsonProcessingException {
        List<Transaction> allTransactions = transactionService.getAllTransactions();
        return new ResponseEntity<>(objectMapper.writeValueAsString(allTransactions), HttpStatus.OK);
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody TransactionDTO transactionDto) {
        transactionDto.setTransaction_id(UUIDGenerator.generateUUID());
        transactionDto.setTransaction_time(Timestamper.stamp());
        transactionService.deposit(transactionDto);
        return ResponseEntity.accepted().body("Transaction Accepted. TransactionId: " + transactionDto.getTransaction_id());
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody TransactionDTO transactionDto) {
        transactionDto.setTransaction_id(UUIDGenerator.generateUUID());
        transactionDto.setAmount(transactionDto.getAmount().negate());
        transactionDto.setTransaction_time(Timestamper.stamp());

        transactionService.deposit(transactionDto);
        return ResponseEntity.accepted().body("Transaction Accepted");
    }

    @PostMapping("/send-receive")
    public ResponseEntity<String> sendMoney(@RequestBody SendReceiveTransactionDto request)
            throws CloneNotSupportedException {
        TransactionDTO sendDto = TransactionDTO.builder()
                .amount(request.getAmount().negate())
                .currency(request.getCurrency())
                .transaction_time(Timestamper.stamp())
                .account_id(request.getFrom_account())
                .transaction_id(UUIDGenerator.generateUUID())
                .status("Pending")
                .build();

        transactionService.deposit(sendDto);
        TransactionDTO receiveDto = (TransactionDTO) sendDto.clone();
        receiveDto.setAmount(request.getAmount());
        receiveDto.setAccount_id(request.getTo_account());
        transactionService.deposit(receiveDto);
        return ResponseEntity.accepted().body("transaction processing");

    }
}