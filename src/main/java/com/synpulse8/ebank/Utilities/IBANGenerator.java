package com.synpulse8.ebank.Utilities;

import com.synpulse8.ebank.Enums.CountryCode;

import java.util.Random;

public class IBANGenerator {
    public static String generate(CountryCode countryCode){
        String checkDigit = "32";
        String bankCode = "EB12";
        String branchCode = "HK1234";
        long accountNum = new Random().nextLong() % 9000000000000000000L + 1000000000000000000L;
        return countryCode +checkDigit + bankCode + branchCode + accountNum;
    }
}
