package com.synpulse8.ebank.Exceptions;

public class TokenNotFoundException extends RuntimeException {
    public TokenNotFoundException(String invalid_refresh_token) {
    }
}
