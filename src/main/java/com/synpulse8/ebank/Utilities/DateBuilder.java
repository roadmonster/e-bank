package com.synpulse8.ebank.Utilities;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateBuilder {
    public static Date formDate(int y, int m, int d) {
        LocalDate localDate = LocalDate.of(y, m, d);
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
