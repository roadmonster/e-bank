package com.synpulse8.ebank.Exceptions;

public class BankTransactionNotFoundException extends RuntimeException {
    public BankTransactionNotFoundException(String give_transaction_id_not_exisit) {
    }
}
