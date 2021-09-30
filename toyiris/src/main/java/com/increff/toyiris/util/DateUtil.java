package com.increff.toyiris.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import static java.time.temporal.ChronoUnit.DAYS;

public class DateUtil {
    public static int differenceInDays(LocalDate start,LocalDate end)
    {
        return (int)DAYS.between(start,end);
    }
    public static boolean validateData(String dataString){
        Date date;
        try {
            date = new SimpleDateFormat("dd/MM/yyyy").parse(dataString);
        } catch (ParseException parseException) {
            return false;
        }
        return true;    }
}
