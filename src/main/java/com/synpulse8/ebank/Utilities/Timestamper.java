package com.synpulse8.ebank.Utilities;

import java.sql.Timestamp;

public class Timestamper {
    public static Timestamp stamp(){
        long currentTimeMillis = System.currentTimeMillis();
        return new Timestamp(currentTimeMillis);
    }
}
