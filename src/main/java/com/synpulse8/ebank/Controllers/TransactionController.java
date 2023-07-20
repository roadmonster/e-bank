package com.synpulse8.ebank.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.synpulse8.ebank.DTO.*;
import com.synpulse8.ebank.Enums.MoneyDirection;
import com.synpulse8.ebank.Exceptions.BankTransactionNotFoundException;
import com.synpulse8.ebank.Models.Transaction;
import com.synpulse8.ebank.Services.TransactionService;
import com.synpulse8.ebank.Utilities.Timestamper;
import com.synpulse8.ebank.Utilities.UUIDGenerator;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transaction")
@AllArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final ObjectMapper objectMapper = new ObjectMapper();

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

    @GetMapping("/{year}/{month}/{day}")
    public ResponseEntity<String> getTransactionForCertainDay(@PathVariable int year,
                                                              @PathVariable int month,
                                                              @PathVariable int day) throws JsonProcessingException {
        List<Transaction> transactions = transactionService.getTransactionByDate(year, month, day);
        return new ResponseEntity<>(objectMapper.writeValueAsString(transactions), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<String> getTransactionBetweenDate(@RequestParam int fYear,
                                                            @RequestParam int fMonth,
                                                            @RequestParam int fDay,
                                                            @RequestParam int tYear,
                                                            @RequestParam int tMonth,
                                                            @RequestParam int tDay) throws JsonProcessingException {
        Date fromDate = formDate(fYear, fMonth, fDay);
        Date toDate = formDate(tYear, tMonth, tDay);
        return ResponseEntity.ok(objectMapper.writeValueAsString(
                transactionService.getTransactionBetween(fromDate,toDate)));
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
        return ResponseEntity.accepted().body("Transaction Accepted" + transactionDto.getTransaction_id());
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
                .userId(request.getSender_id())
                .status("Pending")
                .build();
        UUID transaction_id_to_return = sendDto.getTransaction_id();
        transactionService.deposit(sendDto);

        TransactionDTO receiveDto = (TransactionDTO) sendDto.clone();
        receiveDto.setAmount(request.getAmount());
        receiveDto.setAccount_id(request.getTo_account());
        receiveDto.setUserId(request.getReceiver_id());
        receiveDto.setTransaction_id(UUIDGenerator.generateUUID());
        transactionService.deposit(receiveDto);
        if (request.getDirection() == MoneyDirection.incoming) {
            transaction_id_to_return = receiveDto.getTransaction_id();
        }
        return ResponseEntity.accepted().body("transaction processing " + transaction_id_to_return);
    }

    private Date formDate(int y, int m, int d) {
        LocalDate localDate = LocalDate.of(y, m, d);
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}